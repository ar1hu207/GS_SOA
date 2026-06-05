package br.com.fiap.helios.rest.external;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Integração com a <b>NASA POWER</b> (consumo de serviço REST externo).
 *
 * <p>A NASA POWER fornece, por satélite, a irradiância solar e a temperatura de
 * um ponto (lat/lon). O HÉLIOS traduz esses dados nos sinais de ambiente que o
 * diagnóstico usa: irradiância baixa → {@code solBaixo}, temperatura baixa →
 * {@code frio}. É "tecnologia espacial aplicada a um problema real" (Space Connect).</p>
 *
 * <p>Resiliente: se a chamada falhar ou não houver dado, retorna ambiente simulado.</p>
 */
@Service
public class AmbienteService {

    private static final Logger log = LoggerFactory.getLogger(AmbienteService.class);
    private static final DateTimeFormatter YMD = DateTimeFormatter.BASIC_ISO_DATE; // yyyyMMdd
    private static final double FILL = -990.0; // NASA POWER usa -999 para dado ausente

    private final RestClient http = RestClient.create();
    private final String baseUrl;
    private final double limiarIrradiancia;
    private final double limiarFrio;

    public AmbienteService(
            @Value("${helios.ambiente.nasa.url:https://power.larc.nasa.gov/api/temporal/daily/point}") String baseUrl,
            @Value("${helios.ambiente.nasa.limiar-irradiancia:2.5}") double limiarIrradiancia,
            @Value("${helios.ambiente.nasa.limiar-frio:10.0}") double limiarFrio) {
        this.baseUrl = baseUrl;
        this.limiarIrradiancia = limiarIrradiancia;
        this.limiarFrio = limiarFrio;
    }

    public Ambiente consultar(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return Ambiente.simulado();
        }
        try {
            LocalDate hoje = LocalDate.now();
            String inicio = hoje.minusDays(30).format(YMD);
            String fim = hoje.minusDays(2).format(YMD); // POWER tem alguns dias de latência

            String url = String.format(Locale.US,
                    "%s?parameters=ALLSKY_SFC_SW_DWN,T2M&community=RE"
                            + "&latitude=%s&longitude=%s&start=%s&end=%s&format=JSON",
                    baseUrl, latitude, longitude, inicio, fim);

            JsonNode json = http.get().uri(URI.create(url)).retrieve().body(JsonNode.class);
            return interpretar(json);
        } catch (Exception e) {
            log.warn("Falha ao consultar NASA POWER, usando ambiente simulado: {}", e.getMessage());
            return Ambiente.simulado();
        }
    }

    /** Pega o dia mais recente com dado válido e deriva os sinais de ambiente. */
    private Ambiente interpretar(JsonNode json) {
        if (json == null) {
            return Ambiente.simulado();
        }
        JsonNode parametros = json.path("properties").path("parameter");
        JsonNode irradiancia = parametros.path("ALLSKY_SFC_SW_DWN");
        JsonNode temperatura = parametros.path("T2M");

        List<String> datas = new ArrayList<>();
        irradiancia.fieldNames().forEachRemaining(datas::add);
        datas.sort(Comparator.reverseOrder());

        for (String dia : datas) {
            double irr = irradiancia.path(dia).asDouble(FILL);
            if (irr <= FILL) {
                continue; // dado ausente, tenta o dia anterior
            }
            double temp = temperatura.path(dia).asDouble(FILL);
            boolean solBaixo = irr < limiarIrradiancia;
            boolean frio = temp > FILL && temp < limiarFrio;
            return new Ambiente(solBaixo, frio, "nasa-power");
        }
        return Ambiente.simulado();
    }
}
