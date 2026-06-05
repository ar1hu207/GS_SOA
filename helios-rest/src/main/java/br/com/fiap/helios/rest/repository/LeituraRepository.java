package br.com.fiap.helios.rest.repository;

import br.com.fiap.helios.rest.domain.Leitura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeituraRepository extends JpaRepository<Leitura, Long> {

    long countByPainelId(Long painelId);
}
