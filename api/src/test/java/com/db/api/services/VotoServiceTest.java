package com.db.api.services;

import com.db.api.dtos.VotoDto;
import com.db.api.enums.StatusCPF;
import com.db.api.exceptions.NaoPodeVotarException;
import com.db.api.exceptions.VotoDuplicadoException;
import com.db.api.models.Associado;
import com.db.api.models.Sessao;
import com.db.api.models.Voto;
import com.db.api.repositories.VotoRepository;
import com.db.api.stubs.AssociadoStub;
import com.db.api.stubs.SessaoStub;
import com.db.api.stubs.VotoStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validator;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private SessaoService sessaoService;

    @Mock
    private Validator validator;

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

        when(validator.validate(votoDto)).thenReturn(Collections.emptySet());
        when(sessaoService.validarSessao(votoDto.getSessao_id())).thenReturn(sessao);
        when(associadoService.buscarAssociadoPorCPF(votoDto.getAssociado().getCpf())).thenReturn(associado);
        when(votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf())).thenReturn(false);

        assertDoesNotThrow(() -> votoService.registrarVoto(votoDto));

        verify(votoRepository, times(1)).save(any(Voto.class));
    }


    @Test
    void testRegistrarVotoDuplicado() {
        when(validator.validate(votoDto)).thenReturn(Collections.emptySet());
        when(votoRepository.existsVotoBySessaoIdAndAssociadoCpf(votoDto.getSessao_id(), votoDto.getAssociado().getCpf())).thenReturn(true);

        assertThrows(VotoDuplicadoException.class, () -> votoService.registrarVoto(votoDto));
    }

}
