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
    @DisplayName("Deve registrar um voto com sucesso caso todos os dados sejam validos")
    void testRegistrarVoto() {

        when(sessaoService.validarSessao(votoDto.getSessao_id())).thenReturn(sessao);
        when(associadoService.buscarAssociadoPorCPF(votoDto.getAssociado().getCpf())).thenReturn(associado);
        when(votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf())).thenReturn(false);

        assertDoesNotThrow(() -> votoService.registrarVoto(votoDto));

        verify(votoRepository, times(1)).save(any(Voto.class));
    }


    @Test
    @DisplayName("Deve retornar uma exceção caso o mesmo associado tente votar mais de uma vez na mesma sessão")
    void testRegistrarVotoDuplicado() {
        when(votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf())).thenReturn(true);

        assertThrows(VotoDuplicadoException.class, () -> votoService.registrarVoto(votoDto));
    }

}
