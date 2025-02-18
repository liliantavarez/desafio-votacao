package com.db.api.models;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private final String mensagem;
    private final List<String> detalhes;

    public ErrorResponse(String message, List<String> detalhes) {
        this.mensagem = message;
        this.detalhes = detalhes;
    }
}
