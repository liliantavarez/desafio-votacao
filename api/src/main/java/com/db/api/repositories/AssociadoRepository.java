package com.db.api.repositories;

import com.db.api.models.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    Optional<Associado> findByCpf(String cpfAssociado);

    boolean existsAssociadoByCpf(String cpfAssociado);
}
