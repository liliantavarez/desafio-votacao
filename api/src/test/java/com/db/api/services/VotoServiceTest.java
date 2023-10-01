package com.db.api.services;

import com.db.api.dtos.VotoDto;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.exceptions.VotoDuplicadoException;
import com.db.api.models.Associado;
import com.db.api.models.Sessao;
import com.db.api.models.Voto;
import com.db.api.repositories.VotoRepository;
import com.db.api.stubs.AssociadoStub;
import com.db.api.stubs.SessaoStub;
import com.db.api.stubs.VotoStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private SessaoService sessaoService;

    @InjectMocks
    private VotoService votoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final VotoDto votoDto = VotoStub.gerarVotoDtoValida();
    private final Sessao sessao = SessaoStub.gerarSessaoAberta();
    private final Associado associado = AssociadoStub.gerarAssociadoDtoValida();

    @Test
    void testRegistrarVoto() {

        when(sessaoService.validarSessao(votoDto.getSessao_id())).thenReturn(sessao);
        when(associadoService.buscarAssociadoPorCPF(votoDto.getAssociado().getCpf())).thenReturn(associado);
        when(votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf())).thenReturn(false);

        assertDoesNotThrow(() -> votoService.registrarVoto(votoDto));

        verify(votoRepository, times(1)).save(any(Voto.class));
    }


    @Test
    void testRegistrarVotoDuplicado() {
        when(votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf())).thenReturn(true);

        assertThrows(VotoDuplicadoException.class, () -> votoService.registrarVoto(votoDto));
    }
    @Test
    @DisplayName("Deve buscar um voto com determinado id com sucesso")
    void testObterVotoPorID() {
        Voto voto = VotoStub.gerarVotoValido();
        when(votoRepository.findById(voto.getId())).thenReturn(Optional.of(voto));

        Voto votoEncontrada = votoService.buscarVotoPorID(voto.getId());

        assertEquals(voto, votoEncontrada);
    }

    @Test
    @DisplayName("Deve retornar uma exceção ao buscar um voto por id inexistente ")
    void testObterVotoPorIDInexistente() {
        when(votoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> votoService.buscarVotoPorID(1L));
    }
}
