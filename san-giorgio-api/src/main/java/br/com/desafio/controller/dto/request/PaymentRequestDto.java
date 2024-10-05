package br.com.desafio.controller.dto.request;

import br.com.desafio.domain.model.PaymentModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PaymentRequestDto(
        @JsonProperty("client_id")
        @NotBlank
        String clientId,
        @JsonProperty("payment_items")
        @NotEmpty
        @Valid
        List<PaymentItemRequestDto> paymentItems) {
    public PaymentModel toPaymentModel() {
        return new PaymentModel(
                clientId(),
                paymentItems().stream()
                        .map(PaymentItemRequestDto::toPaymentItemModel)
                        .toList()
        );
    }

}
