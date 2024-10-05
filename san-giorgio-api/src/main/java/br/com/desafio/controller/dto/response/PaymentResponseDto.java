package br.com.desafio.controller.dto.response;

import br.com.desafio.domain.model.PaymentModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaymentResponseDto(@JsonProperty("client_id")
                                 String clientId,
                                 @JsonProperty("payment_items")
                                 List<PaymentItemResponseDto> paymentItems) {

    public static synchronized PaymentResponseDto toDto(PaymentModel paymentModel) {
        return new PaymentResponseDto(
                paymentModel.clientId(),
                paymentModel.paymentItems().stream()
                        .map(PaymentItemResponseDto::toDto)
                        .toList()
        );
    }

}
