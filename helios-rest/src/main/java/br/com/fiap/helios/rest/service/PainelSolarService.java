package br.com.fiap.helios.rest.service;

import br.com.fiap.helios.rest.domain.PainelSolar;
import br.com.fiap.helios.rest.dto.PainelSolarRequest;
import br.com.fiap.helios.rest.exception.RecursoNaoEncontradoException;
import br.com.fiap.helios.rest.exception.RegraNegocioException;
import br.com.fiap.helios.rest.repository.AlertaRepository;
import br.com.fiap.helios.rest.repository.DiagnosticoRepository;
import br.com.fiap.helios.rest.repository.LeituraRepository;
import br.com.fiap.helios.rest.repository.PainelSolarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Regras de negócio do CRUD de painéis (encapsula validações e acesso ao repositório). */
@Service
public class PainelSolarService {

    private final PainelSolarRepository repository;
    private final LeituraRepository leituraRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final AlertaRepository alertaRepository;

    public PainelSolarService(PainelSolarRepository repository,
                              LeituraRepository leituraRepository,
                              DiagnosticoRepository diagnosticoRepository,
                              AlertaRepository alertaRepository) {
        this.repository = repository;
        this.leituraRepository = leituraRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.alertaRepository = alertaRepository;
    }

    @Transactional(readOnly = true)
    public List<PainelSolar> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public PainelSolar buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Painel não encontrado para o id " + id));
    }

    @Transactional
    public PainelSolar criar(PainelSolarRequest req) {
        if (repository.existsByCodigo(req.codigo())) {
            throw new RegraNegocioException("Já existe um painel com o código " + req.codigo());
        }
        PainelSolar painel = new PainelSolar();
        aplicar(painel, req);
        return repository.save(painel);
    }

    @Transactional
    public PainelSolar atualizar(Long id, PainelSolarRequest req) {
        PainelSolar painel = buscarPorId(id);
        repository.findByCodigo(req.codigo())
                .filter(outro -> !outro.getId().equals(id))
                .ifPresent(outro -> {
                    throw new RegraNegocioException(
                            "O código " + req.codigo() + " já pertence a outro painel.");
                });
        aplicar(painel, req);
        return repository.save(painel);
    }

    @Transactional
    public void remover(Long id) {
        PainelSolar painel = buscarPorId(id);
        long historico = leituraRepository.countByPainelId(id)
                + diagnosticoRepository.countByPainelId(id)
                + alertaRepository.countByPainelId(id);
        if (historico > 0) {
            throw new RegraNegocioException("Painel " + painel.getCodigo()
                    + " possui histórico de telemetria (" + historico
                    + " registros) e não pode ser removido. Desative-o (PUT com ativo=false)"
                    + " ou remova o histórico antes.");
        }
        repository.delete(painel);
    }

    private void aplicar(PainelSolar painel, PainelSolarRequest req) {
        painel.setCodigo(req.codigo());
        painel.setNome(req.nome());
        painel.setPotenciaNominalW(req.potenciaNominalW());
        painel.setLatitude(req.latitude());
        painel.setLongitude(req.longitude());
        painel.setAtivo(req.ativo() == null || req.ativo());
    }
}
