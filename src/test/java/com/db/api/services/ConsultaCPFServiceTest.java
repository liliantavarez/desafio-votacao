package com.db.api.services;

import com.db.api.client.ConsultaCpfClient;
import com.db.api.client.response.ConsultaCPFResponse;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("consultaCPFServiceTest")
class ConsultaCPFServiceTest {

    @Mock
    private ConsultaCpfClient consultaCpfClient;

    @InjectMocks
    private ConsultaCPFService consultaCPFService;

    private final ConsultaCPFResponse consultaCPFResponse = new ConsultaCPFResponse();

    @Test
    void testVerificarSituacaoCPFRegular() {
        String cpfRegular = "12345678901";
        when(consultaCpfClient.verificarSituacaoCPF(Mockito.anyString())).thenReturn(consultaCPFResponse);

        ConsultaCPFResponse result = consultaCPFService.verificarSituacaoCPF(cpfRegular);

        assertEquals(consultaCPFResponse, result);
    }

    @Test
    void testVerificarSituacaoCPFIrregular() {
        String cpfIrregular = "00000000000";
        when(consultaCpfClient.verificarSituacaoCPF(Mockito.anyString()))
                .thenThrow(FeignException.BadRequest.class);

        assertThrows(FeignException.class, () ->
                consultaCPFService.verificarSituacaoCPF(cpfIrregular));
    }

}