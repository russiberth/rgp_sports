package com.pcs.rgpsports.exception;

// Exceção lançada quando um recurso não é encontrado no banco de dados
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}
