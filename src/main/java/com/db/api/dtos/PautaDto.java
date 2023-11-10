package com.db.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PautaDto {
    private long id;
    private String titulo;
    private String descricao;

    public PautaDto(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }
}
