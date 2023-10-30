package com.db.api.services;

import com.db.api.client.ConsultaCpfClient;
import com.db.api.client.response.ConsultaCPFResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsultaCPFService {

    private final ConsultaCpfClient consultaCpfClient;

    public ConsultaCPFResponse verificarSituacaoCPF(String cpf) {
        return consultaCpfClient.verificarSituacaoCPF(cpf);
    }
}