package com.db.api.controllers;

import com.db.api.dtos.VotoDto;
import com.db.api.services.VotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/votos")
public class VotoController {

    private final VotoService votoService;

    @PostMapping("/salvar")
    ResponseEntity<String> registrarVoto(@RequestBody @Valid VotoDto votoDto) {
        votoService.registrarVoto(votoDto);


        return ResponseEntity.ok().body("Voto salvo com sucesso!");
    }

}
