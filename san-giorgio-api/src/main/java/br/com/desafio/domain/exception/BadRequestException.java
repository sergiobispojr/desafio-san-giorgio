package br.com.desafio.domain.exception;

public class BadRequestException extends SanGiorgioApiException {
    public BadRequestException(String message) {
        super(message);
    }
}
