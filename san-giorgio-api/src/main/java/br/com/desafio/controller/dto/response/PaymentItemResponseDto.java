package br.com.desafio.controller.dto.response;

import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PaymentItemResponseDto(@JsonProperty("payment_id")
                                     String paymentId,
                                     @JsonProperty("payment_value")
                                     BigDecimal paymentValue,
                                     @JsonProperty("payment_status")
                                     PaymentStatus paymentStatus) {

    public static synchronized PaymentItemResponseDto toDto(PaymentItemModel paymentItemModel) {
        return new PaymentItemResponseDto(
                paymentItemModel.paymentId(),
                paymentItemModel.paymentValue(),
                paymentItemModel.paymentStatus()
        );
    }

}
