package br.com.desafio.util;

import br.com.desafio.domain.exception.BadRequestException;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class BindingResultUtil {
    private BindingResultUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static synchronized void verifyBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    bindingResult.getFieldErrors().stream()
                            .map(e ->
                                    "field:[" + e.getField() +
                                            "] message:[" + e.getDefaultMessage()+"]")
                            .collect(Collectors.joining(" || ")));
        }
    }
}
