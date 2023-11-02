package com.db.api.client;

import com.db.api.client.response.ConsultaCPFResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ConsultaCpf", url = "${url.api.consulta.cpf}")
public interface ConsultaCpfClient {
    @GetMapping("/{cpf}")
    ConsultaCPFResponse verificarSituacaoCPF(@PathVariable("cpf") String cpf);
}
