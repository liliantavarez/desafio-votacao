package com.db.api.controllers;

import com.db.api.client.response.ConsultaCPFResponse;
import com.db.api.services.ConsultaCPFService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/consultaCPF")
public class ConsultaCPFController {

    private final ConsultaCPFService consultaCPFService;

    @GetMapping("/{cpf}")
    ResponseEntity<ConsultaCPFResponse> verificarSituacaoCPF(@PathVariable String cpf) {
        return ResponseEntity.ok(consultaCPFService.verificarSituacaoCPF(cpf));
    }
}