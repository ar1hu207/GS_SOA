package br.com.fiap.helios.rest.controller;

import br.com.fiap.helios.rest.dto.DiagnosticoResponse;
import br.com.fiap.helios.rest.dto.LeituraRequest;
import br.com.fiap.helios.rest.dto.TelemetriaResponse;
import br.com.fiap.helios.rest.repository.DiagnosticoRepository;
import br.com.fiap.helios.rest.service.TelemetriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Telemetria de painéis: dispara o loop e consulta o histórico de diagnósticos. */
@RestController
@RequestMapping("/api/paineis/{painelId}")
@Tag(name = "Telemetria & Diagnóstico", description = "Loop perceber → diagnosticar → agir")
public class TelemetriaController {

    private final TelemetriaService telemetriaService;
    private final DiagnosticoRepository diagnosticoRepository;

    public TelemetriaController(TelemetriaService telemetriaService,
                                DiagnosticoRepository diagnosticoRepository) {
        this.telemetriaService = telemetriaService;
        this.diagnosticoRepository = diagnosticoRepository;
    }

    @PostMapping("/leituras")
    @Operation(summary = "Envia telemetria e executa o loop (ambiente → SOAP → alerta/comando)")
    public TelemetriaResponse enviarLeitura(@PathVariable Long painelId,
                                            @Valid @RequestBody LeituraRequest req) {
        return telemetriaService.processar(painelId, req);
    }

    @GetMapping("/diagnosticos")
    @Operation(summary = "Lista os diagnósticos do painel (mais recentes primeiro)")
    public List<DiagnosticoResponse> listarDiagnosticos(@PathVariable Long painelId) {
        return diagnosticoRepository.findByPainelIdOrderByCriadoEmDesc(painelId)
                .stream().map(DiagnosticoResponse::de).toList();
    }
}
