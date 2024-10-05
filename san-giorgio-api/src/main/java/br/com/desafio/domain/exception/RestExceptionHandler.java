package br.com.desafio.domain.exception;

import br.com.desafio.controller.dto.ApiErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler( { EntityNotFoundException.class } )
    public ResponseEntity<ApiErrorDto> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request){

        ApiErrorDto err = new ApiErrorDto();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Recurso não encontrado");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler( { BadRequestException.class } )
    public ResponseEntity<ApiErrorDto> handleBadRequestException(BadRequestException e, HttpServletRequest request){

        ApiErrorDto err = new ApiErrorDto();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Erro na requisição");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }

    @ExceptionHandler( { SanGiorgioApiException.class, RuntimeException.class } )
    public ResponseEntity<ApiErrorDto> handlePeopleHubException(SanGiorgioApiException e, HttpServletRequest request){

        ApiErrorDto err = new ApiErrorDto();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        err.setError("Erro no sistema :(");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(err.getStatus()).body(err);

    }
}
