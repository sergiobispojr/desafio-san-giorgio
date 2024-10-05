package br.com.desafio.domain.model;

import java.util.List;

public record PaymentModel (
        String clientId,
        List<PaymentItemModel> paymentItems
) {
}
