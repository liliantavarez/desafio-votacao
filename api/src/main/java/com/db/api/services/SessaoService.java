package com.db.api.services;

import com.db.api.dtos.response.SessaoResponse;
import com.db.api.enums.ResultadoSessao;
import com.db.api.enums.StatusSessao;
import com.db.api.enums.VotoEnum;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.exceptions.SessaoEncerradaException;
import com.db.api.models.Pauta;
import com.db.api.models.Sessao;
import com.db.api.repositories.SessaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SessaoService {

    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public Sessao iniciarSessaoVotacao(@Valid String pautaTitulo, @Valid LocalDateTime dataEncerramento) {
        Pauta pauta = buscarPauta(pautaTitulo);
        Sessao sessao = criarSessao(pauta, dataEncerramento);

        if (pauta == null) {
            throw new RegistroNaoEncontradoException("Pauta não encontrada.");
        }

        return sessaoRepository.save(sessao);
    }

    private Pauta buscarPauta(String pautaTitulo) {
        return pautaService.buscarPauta(pautaTitulo);
    }

    private Sessao criarSessao(Pauta pauta, LocalDateTime dataEncerramento) {
        Sessao sessao = new Sessao(pauta);

        if (dataEncerramento != null) {
            sessao.setDataEncerramento(dataEncerramento);
        }

        return sessao;
    }

    @Transactional
    public Sessao validarSessao(long sessaoId) {
        Sessao sessao = buscarSessaoPorID(sessaoId);

        if (sessao.getDataEncerramento().isBefore(LocalDateTime.now()) && sessao.getStatusSessao() == StatusSessao.ABERTA) {
            encerrarSessao(sessao);
        }

        if (sessao.getStatusSessao() == StatusSessao.ENCERRADA) {
            throw new SessaoEncerradaException();
        }

        return sessao;
    }

    @Transactional
    public void encerrarSessao(Sessao sessao) {
        sessao.setStatusSessao(StatusSessao.ENCERRADA);
        sessaoRepository.save(sessao);
    }

    @Transactional
    public SessaoResponse contabilizarVotos(Long sessaoId) {
        Sessao sessao = buscarSessaoPorID(sessaoId);
        if (sessao == null) {
            throw new RegistroNaoEncontradoException("");
        }
        String querySQLContarVotos = "SELECT v.votoEnum, COUNT(v) FROM Voto v WHERE v.sessao.id = :sessaoId GROUP BY v.votoEnum";
        TypedQuery<Object[]> query = entityManager.createQuery(querySQLContarVotos, Object[].class);
        query.setParameter("sessaoId", sessaoId);
        List<Object[]> results = query.getResultList();

        long votosSim = 0;
        long votosNao = 0;
        for (Object[] result : results) {
            VotoEnum votoEnum = (VotoEnum) result[0];
            long count = (long) result[1];

            if (votoEnum == VotoEnum.SIM) {
                votosSim = count;
            } else if (votoEnum == VotoEnum.NAO) {
                votosNao = count;
            }
        }

        definirResultadoSessao(sessao, votosSim, votosNao);

        return new SessaoResponse(sessao.getPauta(), sessao.getDataEncerramento(), sessao.getResultadoSessao());
    }

    private void definirResultadoSessao(Sessao sessao, Long votosSim, Long votosNao) {
        if (votosSim > votosNao) {
            sessao.setResultadoSessao(ResultadoSessao.APROVADA);
        } else if (votosNao > votosSim) {
            sessao.setResultadoSessao(ResultadoSessao.REPROVADA);
        } else {
            sessao.setResultadoSessao(ResultadoSessao.INDEFINIDA);
        }
    }

    public Sessao buscarSessaoPorID(Long sessaoId) {
        return sessaoRepository.findById(sessaoId).orElseThrow(() -> new RegistroNaoEncontradoException("Sessão não encontrada."));
    }
}

