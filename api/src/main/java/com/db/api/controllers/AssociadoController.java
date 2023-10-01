package com.db.api.controllers;

import com.db.api.dtos.AssociadoDto;
import com.db.api.models.Associado;
import com.db.api.services.AssociadoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    private final AssociadoService associadoService;

    @PostMapping("/cadastrar")
    ResponseEntity<AssociadoDto> registrarAssociado(@RequestBody @Valid AssociadoDto associadoDto, UriComponentsBuilder uriBuilder) {
        associadoService.registrarAssociado(associadoDto);
        Associado associado = new Associado(associadoDto.getNome(), associadoDto.getNome());

        var uri = uriBuilder.path("/associados/{id}").buildAndExpand(associado.getId()).toUri();

        return ResponseEntity.created(uri).body(associadoDto);
    }

    @GetMapping("/{id}")
    ResponseEntity<AssociadoDto> buscarAssociadoPorID(@PathVariable Long id){
        Associado associado = associadoService.buscarAssociadoPorID(id);

        return ResponseEntity.ok(new AssociadoDto(associado.getNome(), associado.getCpf()));
    }

}