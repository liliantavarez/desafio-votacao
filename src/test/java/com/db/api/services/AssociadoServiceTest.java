package com.db.api.services;

import com.db.api.client.response.ConsultaCPFResponse;
import com.db.api.dtos.AssociadoDto;
import com.db.api.dtos.request.AssociadoRequest;
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

@DisplayName("associadoServiceTest")
class AssociadoServiceTest {

    @Mock
    private AssociadoRepository associadoRepository;
    @Mock
    private ConsultaCPFService consultaCPFService;
    @InjectMocks
    private AssociadoService associadoService;

    private final ConsultaCPFResponse consultaCPFResponse = new ConsultaCPFResponse();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ao criar um novo associado com valores válidos, o método save do repositório deve ser chamado")
    void registrarAssociado_ComValoresValidos() {
        AssociadoRequest associadoRequest = AssociadoStub.gerarAssociadoRequest();

        when(associadoRepository.save(any(Associado.class))).thenReturn(AssociadoStub.gerarAssociadoDtoValida());

        AssociadoDto associadoDto = associadoService.cadastrarAssociado(associadoRequest);

        verify(associadoRepository, Mockito.times(1)).save(any(Associado.class));
        assertEquals(associadoRequest.getNome(), associadoDto.getNome());
        assertEquals(associadoRequest.getCpf(), associadoDto.getCpf());
    }

    @Test
    @DisplayName("Ao tentar cadastrar um associado com CPF já cadastrado, deve retornar exceção de associado já cadastrado")
    void registrarAssociado_ComCpfJaCadastrado() {
        AssociadoRequest associadoRequest = AssociadoStub.gerarAssociadoRequest();
        when(associadoRepository.existsAssociadoByCpf(associadoRequest.getCpf())).thenReturn(true);

        assertThrows(AssociadoJaCadastradoException.class, () ->
                associadoService.cadastrarAssociado(associadoRequest));
    }

    @Test
    @DisplayName("Deve retornar uma exceção para associado com cpf inapto para votar")
    void testValidarSeAssociadoPodeVotar() {
        Associado associado = AssociadoStub.gerarAssociadoDtoValida();

        when(associadoRepository.findByCpf(associado.getCpf())).thenReturn(Optional.of(associado));

        consultaCPFResponse.setSituacao("Cancelada");
        when(consultaCPFService.verificarSituacaoCPF(associado.getCpf())).thenReturn(consultaCPFResponse);

        assertThrows(NaoPodeVotarException.class, () ->
                associadoService.validarAssociadoParaVotacao(associado.getCpf()));
    }

    @Test
    @DisplayName("Deve buscar um associado com determinado id com sucesso")
    void testObterAssociadoPorID() {
        Associado associado = AssociadoStub.gerarAssociadoDtoValida();
        when(associadoRepository.findById(associado.getId())).thenReturn(Optional.of(associado));

        AssociadoDto associadoEncontrada = associadoService.buscarAssociadoPorID(associado.getId());

        assertEquals(associado.getCpf(), associadoEncontrada.getCpf());
    }

    @Test
    @DisplayName("Deve retornar uma exceção ao buscar um associado por id inexistente ")
    void testObterAssociadoPorIDInexistente() {
        when(associadoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> associadoService.buscarAssociadoPorID(1L));
    }
}
