package com.db.api.controllers;

import com.db.api.dtos.VotoDto;
import com.db.api.dtos.request.AssociadoRequestVoto;
import com.db.api.models.Voto;
import com.db.api.services.VotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/votos")
public class VotoController {

    private final VotoService votoService;

    @PostMapping()
    ResponseEntity<String> registrarVoto(@RequestBody @Valid VotoDto votoDto) {
        votoService.registrarVoto(votoDto);

        return ResponseEntity.ok().body("Voto salvo com sucesso!");
    }

}
