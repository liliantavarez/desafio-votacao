package com.db.api.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class AssociadoRequest {

    @NotBlank(message = "Por favor informe o nome do associado!")
    @Size(min = 3, max = 150)
    private final String nome;
    @CPF
    private final String cpf;

}
