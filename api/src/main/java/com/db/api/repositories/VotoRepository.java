package com.db.api.repositories;

import com.db.api.enums.VotoEnum;
import com.db.api.models.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsVotoBySessaoIdAndAssociadoCpf(Long sessaoId, String cpfAssociado);
    int countVotoByVotoEnumAndAndSessaoId(VotoEnum votoEnum, Long sessaoId);
}
