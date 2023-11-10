package com.db.api.services;

import com.db.api.dtos.PautaDto;
import com.db.api.dtos.request.PautaRequest;
import com.db.api.exceptions.ParametrosInvalidosException;
import com.db.api.exceptions.RegistroNaoEncontradoException;
import com.db.api.models.Pauta;
import com.db.api.repositories.PautaRepository;
import com.db.api.stubs.PautaStub;
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

@DisplayName("pautaServiceTest")
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Ao criar uma nova pauta com valores válidos, o método save do repositório deve ser chamado")
    void criarNovaPauta_comValoresValidos() {
        PautaRequest pautaRequest = PautaStub.gerarPautaRequest();

        when(pautaRepository.save(any(Pauta.class))).thenReturn(PautaStub.gerarPautaDtoValida());

        PautaDto pautaCadastrada = pautaService.criarNovaPauta(pautaRequest);

        verify(pautaRepository, Mockito.times(1)).save(any(Pauta.class));
        assertEquals(pautaCadastrada.getTitulo(), pautaRequest.getTitulo());
        assertEquals(pautaCadastrada.getDescricao(), pautaRequest.getDescricao());
    }

    @Test
    @DisplayName("Ao criar uma nova pauta sem titulo, deve retornar exceção")
    void criarNovaPauta_comTituloEmBranco() {
        PautaRequest pautaRequest = PautaStub.gerarPautaDtoTituloVazio();

        assertThrows(ParametrosInvalidosException.class, () ->
                pautaService.criarNovaPauta(pautaRequest));
    }

    @Test
    @DisplayName("Deve buscar uma pauta com determinado id com sucesso")
    void testObterPautaPorID() {
        Pauta pauta = PautaStub.gerarPautaDtoValida();
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));

        Pauta pautaEncontrada = pautaService.buscarPautaPorID(pauta.getId());

        assertEquals(pauta, pautaEncontrada);
    }

    @Test
    @DisplayName("Deve retornar uma exceção ao buscar uma pauta por id inexistente ")
    void testObterPautaPorIDInexistente() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> pautaService.buscarPautaPorID(1L));
    }
}
