package com.db.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VotoDuplicadoException extends RuntimeException {
    public VotoDuplicadoException() {
        super("Este associado já votou nesta sessão.");
    }
}
