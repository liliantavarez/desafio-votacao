package com.db.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NaoPodeVotarException extends RuntimeException {
    public NaoPodeVotarException() {
        super("O cpf do associado encontrasse inapto para votação");
    }
}
