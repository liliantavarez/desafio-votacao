package com.db.api.client;

import com.db.api.client.response.ConsultaCPFResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ConsultaCpf", url = "${url.api.consulta.cpf}/5ae973d7a997af13f0aaf2bf60e65803/9")
public interface ConsultaCpfClient {
    @GetMapping("/{cpf}")
    ConsultaCPFResponse verificarSituacaoCPF(@PathVariable("cpf") String cpf);
}
