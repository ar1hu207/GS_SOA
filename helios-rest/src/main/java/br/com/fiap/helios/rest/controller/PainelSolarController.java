package br.com.fiap.helios.rest.controller;

import br.com.fiap.helios.rest.domain.PainelSolar;
import br.com.fiap.helios.rest.dto.PainelSolarRequest;
import br.com.fiap.helios.rest.dto.PainelSolarResponse;
import br.com.fiap.helios.rest.service.PainelSolarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** API REST de painéis solares (CRUD completo da entidade principal do domínio). */
@RestController
@RequestMapping("/api/paineis")
@Tag(name = "Painéis Solares", description = "CRUD dos painéis monitorados pelo HÉLIOS")
public class PainelSolarController {

    private final PainelSolarService service;

    public PainelSolarController(PainelSolarService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todos os painéis")
    public List<PainelSolarResponse> listar() {
        return service.listar().stream().map(PainelSolarResponse::de).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um painel por id")
    public PainelSolarResponse buscar(@PathVariable Long id) {
        return PainelSolarResponse.de(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo painel")
    public ResponseEntity<PainelSolarResponse> criar(@Valid @RequestBody PainelSolarRequest req,
                                                     UriComponentsBuilder uriBuilder) {
        PainelSolar painel = service.criar(req);
        URI uri = uriBuilder.path("/api/paineis/{id}").buildAndExpand(painel.getId()).toUri();
        return ResponseEntity.created(uri).body(PainelSolarResponse.de(painel));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um painel existente")
    public PainelSolarResponse atualizar(@PathVariable Long id,
                                         @Valid @RequestBody PainelSolarRequest req) {
        return PainelSolarResponse.de(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um painel")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
