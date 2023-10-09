package com.db.api.repositories;

import com.db.api.models.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsVotoBySessaoIdAndAssociadoCpf(Long sessaoId, String cpfAssociado);
}
