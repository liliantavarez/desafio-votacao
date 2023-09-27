package com.db.api.services;

import com.db.api.dtos.response.SessaoResponse;
import com.db.api.enums.ResultadoSessao;
import com.db.api.enums.VotoEnum;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.models.Pauta;
import com.db.api.models.Sessao;
import com.db.api.repositories.SessaoRepository;
import com.db.api.repositories.VotoRepository;
import com.db.api.stubs.PautaStub;
import com.db.api.stubs.SessaoStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SessaoServiceTest {

    private SessaoService sessaoService;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;
    @Mock
    private VotoRepository votoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessaoService = new SessaoService(sessaoRepository, pautaService, votoRepository);
    }

    @Test
    @DisplayName("Ao criar iniciar uma nova sessão de votação com valores válidos, o método save do repositório deve ser chamado\"")
    void testIniciarSessaoVotacaComDadosValidos() {

        Pauta pauta = PautaStub.gerarPautaDtoValida();
        when(pautaService.buscarPauta(pauta.getTitulo())).thenReturn(pauta);

        sessaoService.iniciarSessaoVotacao(pauta.getTitulo(), SessaoStub.gerarSessaoDtoValida().getDataEncerramento());

        verify(sessaoRepository, times(1)).save(any(Sessao.class));
    }

    @Test
    @DisplayName("Deve retorna uma exceção caso tente iniciar sessão de votação de uma pauta não cadastrada")
    void testIniciarSessaoVotacaoPautaInexiste() {
        assertThrows(RegistroNaoEncontradoException.class, () -> sessaoService.iniciarSessaoVotacao("Planejamento segundo semestre", null));

        verify(sessaoRepository, never()).save(any(Sessao.class));
    }

    @Test
    @DisplayName("Deve obter resultado de sessão com votos favoráveis")
    void testObterResultadoSessaoAprovada() {
        Sessao sessao = SessaoStub.gerarSessaoEncerrada();

        when(sessaoRepository.findById(sessao.getId())).thenReturn(Optional.of(sessao));
        when(votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.SIM, sessao.getId())).thenReturn(5);
        when(votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.NAO, sessao.getId())).thenReturn(3);

        SessaoResponse response = sessaoService.contabilizarVotos(sessao.getId());

        assertEquals(ResultadoSessao.APROVADA, response.getResultadoSessao());
    }

    @Test
    @DisplayName("Deve obter resultado de sessão com votos contrários")
    void testObterResultadoSessaoReprovada() {
        Sessao sessao = SessaoStub.gerarSessaoEncerrada();

        when(sessaoRepository.findById(sessao.getId())).thenReturn(Optional.of(sessao));
        when(votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.SIM, sessao.getId())).thenReturn(3);
        when(votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.NAO, sessao.getId())).thenReturn(5);

        SessaoResponse response = sessaoService.contabilizarVotos(sessao.getId());

        assertEquals(ResultadoSessao.REPROVADA, response.getResultadoSessao());
    }

    @Test
    @DisplayName("Deve obter resultado de sessão indefinido em caso de empate")
    void testObterResultadoSessaoIndefinida() {
        Sessao sessao = SessaoStub.gerarSessaoEncerrada();
        when(sessaoRepository.findById(sessao.getId())).thenReturn(Optional.of(sessao));
        when(votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.SIM, sessao.getId())).thenReturn(4);
        when(votoRepository.countVotoByVotoEnumAndAndSessaoId(VotoEnum.NAO, sessao.getId())).thenReturn(4);

        SessaoResponse response = sessaoService.contabilizarVotos(sessao.getId());

        assertEquals(ResultadoSessao.INDEFINIDA, response.getResultadoSessao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter resultado de sessão não encontrada")
    void testObterResultadoSessaoNaoEncontrada() {
        Long sessionId = 1L;
        when(sessaoRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> sessaoService.contabilizarVotos(sessionId));
    }


}
