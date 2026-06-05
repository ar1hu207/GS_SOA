package br.com.fiap.helios.rest.repository;

import br.com.fiap.helios.rest.domain.PainelSolar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Repositório JPA do {@link PainelSolar} (CRUD sobre H2). */
public interface PainelSolarRepository extends JpaRepository<PainelSolar, Long> {

    Optional<PainelSolar> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
