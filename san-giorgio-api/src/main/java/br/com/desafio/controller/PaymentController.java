package br.com.desafio.controller;


import br.com.desafio.controller.dto.request.PaymentRequestDto;
import br.com.desafio.controller.dto.response.PaymentResponseDto;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.desafio.util.BindingResultUtil.verifyBindingResult;

@RestController
@RequestMapping(path = "/api/v1")
public class PaymentController {

    private final ConfirmPaymentUseCase service;

    public PaymentController(ConfirmPaymentUseCase service) {
        this.service = service;
    }

    @PutMapping(path = "/payment")
    @Transactional
    public ResponseEntity<PaymentResponseDto> setPayment(
            @RequestBody @Valid PaymentRequestDto request,
            BindingResult bindingResult) {

        verifyBindingResult(bindingResult);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PaymentResponseDto.toDto(
                        service.confirm(request.toPaymentModel())));
    }
}
