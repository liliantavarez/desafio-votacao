package com.db.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssociadoDto {

    private long id;
    private String nome;
    private String cpf;

    public AssociadoDto(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
}
