package br.com.desafio.domain.exception;

public class EntityNotFoundException extends SanGiorgioApiException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
