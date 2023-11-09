package com.db.api.controllers;

import com.db.api.dtos.AssociadoDto;
import com.db.api.dtos.request.AssociadoRequest;
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

    @PostMapping()
    ResponseEntity<AssociadoDto> registrarAssociado(@RequestBody @Valid AssociadoRequest associadoRequest, UriComponentsBuilder uriBuilder) {
        AssociadoDto associadoCadastrado = associadoService.cadastrarAssociado(associadoRequest);

        var uri = uriBuilder.path("/associados/{id}").buildAndExpand(associadoCadastrado.getId()).toUri();

        return ResponseEntity.created(uri).body(associadoCadastrado);
    }

    @GetMapping("/{id}")
    ResponseEntity<AssociadoDto> buscarAssociadoPorID(@PathVariable Long id) {

        return ResponseEntity.ok(associadoService.buscarAssociadoPorID(id));
    }

}