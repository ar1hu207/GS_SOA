package br.com.fiap.helios.rest.repository;

import br.com.fiap.helios.rest.domain.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    List<Diagnostico> findByPainelIdOrderByCriadoEmDesc(Long painelId);

    long countByPainelId(Long painelId);
}
