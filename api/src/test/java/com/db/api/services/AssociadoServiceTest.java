package com.db.api.services;

import com.db.api.dtos.AssociadoDto;
import com.db.api.enums.StatusCPF;
import com.db.api.exceptions.AssociadoJaCadastradoException;
import com.db.api.exceptions.NaoPodeVotarException;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.models.Associado;
import com.db.api.repositories.AssociadoRepository;
import com.db.api.stubs.AssociadoStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Testes para AssociadoService")
class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;

    @InjectMocks
    private AssociadoService associadoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ao criar um novo associado com valores válidos, o método save do repositório deve ser chamado")
    void registrarAssociado_ComValoresValidos() {
        AssociadoDto associadoDto = new AssociadoDto(AssociadoStub.gerarAssociadoDtoValida().getNome(), AssociadoStub.gerarAssociadoDtoValida().getCpf());

        associadoService.registrarAssociado(associadoDto);

        verify(associadoRepository, Mockito.times(1)).save(any(Associado.class));
    }

    @Test
    @DisplayName("Ao tentar cadastrar um associado com CPF já cadastrado, deve retornar exceção de associado já cadastrado")
    void registrarAssociado_ComCpfJaCadastrado() {
        AssociadoDto associadoDto = new AssociadoDto(AssociadoStub.gerarAssociadoDtoValida().getNome(), AssociadoStub.gerarAssociadoDtoValida().getCpf());

        when(associadoRepository.existsAssociadoByCpf(associadoDto.getCpf())).thenReturn(true);

        assertThrows(AssociadoJaCadastradoException.class, () ->
                associadoService.registrarAssociado(associadoDto));
    }

    @Test
    void testValidarSeAssociadoPodeVotar() {
        Associado associado = AssociadoStub.gerarAssociadoDtoValida();
        associado.setStatusCPF(StatusCPF.NAO_PODE_VOTAR);

        when(associadoRepository.findByCpf(associado.getCpf())).thenReturn(Optional.of(associado));

        assertThrows(NaoPodeVotarException.class, () -> associadoService.validarAssociado((associado.getCpf())));
    }

    @Test
    @DisplayName("Deve buscar um associado com determinado id com sucesso")
    void testObterAssociadoPorID() {
        Associado associado = AssociadoStub.gerarAssociadoDtoValida();
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.ofNullable(associado));

        Associado associadoEncontrada = associadoService.buscarAssociadoPorID(associado.getId());

        assertEquals(associado, associadoEncontrada);
    }

    @Test
    @DisplayName("Deve retornar uma exceção ao buscar um associado por id inexistente ")
    void testObterAssociadoPorIDInexistente() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> associadoService.buscarAssociadoPorID(1L));
    }
}
