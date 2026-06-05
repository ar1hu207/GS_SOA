package br.com.fiap.helios.soap.service;

import br.com.fiap.helios.soap.domain.Diagnostico;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Histórico de diagnósticos por painel, em memória.
 *
 * <p>Suficiente para o Web Service responder a {@code consultarHistorico}. A
 * persistência durável dos diagnósticos vive no serviço REST (H2).</p>
 */
@Component
public class HistoricoDiagnostico {

    private final Map<String, List<Diagnostico>> porAtivo = new ConcurrentHashMap<>();

    public void registrar(Diagnostico diagnostico) {
        porAtivo
                .computeIfAbsent(diagnostico.getAtivoId(), k -> new CopyOnWriteArrayList<>())
                .add(diagnostico);
    }

    public List<Diagnostico> consultar(String ativoId) {
        return porAtivo.getOrDefault(ativoId, List.of());
    }
}
