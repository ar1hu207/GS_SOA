package br.com.fiap.helios.rest.repository;

import br.com.fiap.helios.rest.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByResolvidoFalseOrderByCriadoEmDesc();

    long countByPainelId(Long painelId);
}
