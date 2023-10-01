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
import com.db.api.stubs.PautaStub;
import com.db.api.stubs.SessaoStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoService sessaoService;

    @Mock
    private EntityManager entityManager;
    private final Sessao sessaoTeste = SessaoStub.gerarSessaoAberta();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sessaoService = new SessaoService(sessaoRepository, pautaService, entityManager);
    }

    @Test
    @DisplayName("Deve alterar o status da sessão para 'ENCERRADA'")
    void testEncerrarSessao() {
        Sessao sessao = SessaoStub.gerarSessaoAberta();
        when(sessaoRepository.save(sessao)).thenReturn(sessao);

        sessaoService.encerrarSessao(sessao);

        assertEquals(StatusSessao.ENCERRADA, sessao.getStatusSessao());
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
        when(sessaoRepository.findById(sessaoTeste.getId())).thenReturn(Optional.of(sessaoTeste));

        List<VotoEnum> votos = new ArrayList<>();
        votos.add(VotoEnum.SIM);
        votos.add(VotoEnum.NAO);
        votos.add(VotoEnum.SIM);
        when(sessaoRepository.buscarVotosDaSessao(sessaoTeste.getId())).thenReturn(votos);

        SessaoResponse result = sessaoService.contabilizarVotos(sessaoTeste.getId());

        assertEquals(ResultadoSessao.APROVADA, result.getResultadoSessao());
        verify(sessaoRepository, times(1)).buscarVotosDaSessao(sessaoTeste.getId());
    }

    @Test
    @DisplayName("Deve obter resultado de sessão com votos contrários")
    void testObterResultadoSessaoReprovada() {
        when(sessaoRepository.findById(sessaoTeste.getId())).thenReturn(Optional.of(sessaoTeste));

        List<VotoEnum> votos = new ArrayList<>();
        votos.add(VotoEnum.NAO);
        votos.add(VotoEnum.SIM);
        votos.add(VotoEnum.NAO);
        when(sessaoRepository.buscarVotosDaSessao(sessaoTeste.getId())).thenReturn(votos);

        SessaoResponse result = sessaoService.contabilizarVotos(sessaoTeste.getId());

        assertEquals(ResultadoSessao.REPROVADA, result.getResultadoSessao());
        verify(sessaoRepository, times(1)).buscarVotosDaSessao(sessaoTeste.getId());
    }

    @Test
    @DisplayName("Deve obter resultado de sessão indefinido em caso de empate")
    void testObterResultadoSessaoIndefinida() {
        when(sessaoRepository.findById(sessaoTeste.getId())).thenReturn(Optional.of(sessaoTeste));

        List<VotoEnum> votos = new ArrayList<>();
        votos.add(VotoEnum.SIM);
        votos.add(VotoEnum.NAO);
        when(sessaoRepository.buscarVotosDaSessao(sessaoTeste.getId())).thenReturn(votos);

        SessaoResponse result = sessaoService.contabilizarVotos(sessaoTeste.getId());

        assertEquals(ResultadoSessao.INDEFINIDA, result.getResultadoSessao());
        verify(sessaoRepository, times(1)).buscarVotosDaSessao(sessaoTeste.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao obter resultado de sessão não encontrada")
    void testObterResultadoSessaoNaoEncontrada() {
        Long sessionId = 1L;
        when(sessaoRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> sessaoService.contabilizarVotos(sessionId));
    }

    @Test
    @DisplayName("Deve buscar uma sessão com determinado id com sucesso")
    void testObterSessaoPorID() {
        Sessao sessao = SessaoStub.gerarSessaoAberta();
        when(sessaoRepository.findById(sessao.getId())).thenReturn(Optional.of(sessao));

        Sessao sessaoEncontrada = sessaoService.buscarSessaoPorID(sessao.getId());

        assertEquals(sessao, sessaoEncontrada);
    }

    @Test
    @DisplayName("Deve retornar uma exceção ao buscar um associado por id inexistente ")
    void testObterAssociadoPorIDInexistente() {
        when(sessaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> sessaoService.buscarSessaoPorID(1L));
    }
}
