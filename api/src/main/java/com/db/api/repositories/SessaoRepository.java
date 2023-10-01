package com.db.api.repositories;

import com.db.api.enums.VotoEnum;
import com.db.api.models.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
    @Query("SELECT v.votoEnum FROM Voto v WHERE v.sessao.id = :sessaoId")
    List<VotoEnum> buscarVotosDaSessao(@Param("sessaoId") Long sessaoId);
}
