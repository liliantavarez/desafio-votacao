package com.db.api.controllers;

import com.db.api.dtos.PautaDto;
import com.db.api.dtos.request.PautaRequest;
import com.db.api.models.Pauta;
import com.db.api.services.PautaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/pautas")
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    ResponseEntity<PautaDto> criarPauta(@RequestBody @Valid PautaRequest pautaRequest, UriComponentsBuilder uriBuilder) {
        PautaDto pautaCadastrada = pautaService.criarNovaPauta(pautaRequest);

        var uri = uriBuilder.path("/pautas/{id}").buildAndExpand(pautaCadastrada.getId()).toUri();

        return ResponseEntity.created(uri).body(pautaCadastrada);
    }

    @GetMapping("/{id}")
    ResponseEntity<PautaDto> buscarPautaPorID(@PathVariable Long id) {
        Pauta pauta = pautaService.buscarPautaPorID(id);

        return ResponseEntity.ok(new PautaDto(pauta.getTitulo(), pauta.getDescricao()));
    }
}
