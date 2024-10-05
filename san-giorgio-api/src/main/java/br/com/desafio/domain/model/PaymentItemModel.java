package br.com.desafio.domain.model;

import java.math.BigDecimal;

public record PaymentItemModel(String paymentId,
                               BigDecimal paymentValue,
                               PaymentStatus paymentStatus
) {
}
