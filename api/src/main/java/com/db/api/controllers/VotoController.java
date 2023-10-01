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

    @PostMapping("/salvar")
    ResponseEntity<String> registrarVoto(@RequestBody @Valid VotoDto votoDto) {
        votoService.registrarVoto(votoDto);


        return ResponseEntity.ok().body("Voto salvo com sucesso!");
    }

    @GetMapping("/{id}")
    ResponseEntity<VotoDto> buscarAssociadoPorID(@PathVariable Long id) {
        Voto voto = votoService.buscarVotoPorID(id);
        AssociadoRequestVoto associadoRequestVoto = new AssociadoRequestVoto(voto.getAssociado().getCpf());

        return ResponseEntity.ok(new VotoDto(voto.getSessao().getId(), associadoRequestVoto, voto.getVotoEnum()));
    }
}
