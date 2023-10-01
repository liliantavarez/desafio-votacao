package com.db.api.controllers;

import com.db.api.dtos.AssociadoDto;
import com.db.api.dtos.PautaDto;
import com.db.api.models.Associado;
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

    @PostMapping("/cadastrar")
    ResponseEntity<PautaDto> criarPauta(@RequestBody @Valid PautaDto pautaDto, UriComponentsBuilder uriBuilder) {
        pautaService.criarNovaPauta(pautaDto);
        Pauta pauta = new Pauta(pautaDto.getTitulo(), pautaDto.getDescricao());

        var uri = uriBuilder.path("/pautas/{id}").buildAndExpand(pauta.getId()).toUri();

        return ResponseEntity.created(uri).body(pautaDto);
    }
    @GetMapping("/{id}")
    ResponseEntity<PautaDto> buscarPautaPorID(@PathVariable Long id){
        Pauta pauta = pautaService.buscarPautaPorID(id);

        return ResponseEntity.ok(new PautaDto(pauta.getTitulo(), pauta.getDescricao()));
    }
}
