package br.com.fiap.helios.rest.controller;

import br.com.fiap.helios.rest.domain.Alerta;
import br.com.fiap.helios.rest.dto.AlertaResponse;
import br.com.fiap.helios.rest.exception.RecursoNaoEncontradoException;
import br.com.fiap.helios.rest.repository.AlertaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Consulta e gestão de alertas. */
@RestController
@RequestMapping("/api/alertas")
@Tag(name = "Alertas", description = "Alertas gerados pelo diagnóstico")
public class AlertaController {

    private final AlertaRepository alertaRepository;

    public AlertaController(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @GetMapping
    @Operation(summary = "Lista alertas (todos ou apenas os não resolvidos)")
    public List<AlertaResponse> listar(@RequestParam(defaultValue = "false") boolean apenasAbertos) {
        List<Alerta> alertas = apenasAbertos
                ? alertaRepository.findByResolvidoFalseOrderByCriadoEmDesc()
                : alertaRepository.findAll();
        return alertas.stream().map(AlertaResponse::de).toList();
    }

    @PutMapping("/{id}/reconhecer")
    @Operation(summary = "Marca um alerta como resolvido")
    @Transactional
    public AlertaResponse reconhecer(@PathVariable Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Alerta não encontrado para o id " + id));
        alerta.setResolvido(true);
        return AlertaResponse.de(alertaRepository.save(alerta));
    }
}
