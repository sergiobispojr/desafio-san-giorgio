package br.com.desafio.domain.usecase.impl;

import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentModel;
import br.com.desafio.domain.model.PaymentStatus;
import br.com.desafio.domain.usecase.PaymentQueueUseCase;
import br.com.desafio.infraestructure.service.PaymentService;
import br.com.desafio.infraestructure.service.ClientService;
import br.com.desafio.domain.usecase.ConfirmPaymentUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ConfirmPaymentUseCaseImpl implements ConfirmPaymentUseCase {

    private final PaymentService chargeService;
    private final ClientService clientService;

    private final PaymentQueueUseCase sendToExternalQueue;

    public ConfirmPaymentUseCaseImpl(PaymentService chargeService, ClientService clientService, PaymentQueueUseCase sendToExternalQueue) {
        this.chargeService = chargeService;
        this.clientService = clientService;
        this.sendToExternalQueue = sendToExternalQueue;
    }

    @Override
    @Transactional
    public PaymentModel confirm(PaymentModel paymentModel) {
        clientService.findById(paymentModel.clientId());

        List<PaymentItemModel> paymentItemModelList =
                paymentModel.paymentItems().stream()
                        .map(item -> {
                            BigDecimal originalAmount = chargeService.getOriginalAmountFromId(item.paymentId());

                            PaymentStatus paymentStatus = paymentStatus(item.paymentValue(), originalAmount);

                            PaymentItemModel result = new PaymentItemModel(
                                    item.paymentId(),
                                    item.paymentValue(),
                                    paymentStatus
                            );

                            sendToExternalQueue.enqueuePayment(result);

                            return result;
                        })
                        .toList();

        return new PaymentModel(paymentModel.clientId(), paymentItemModelList);

    }

    private PaymentStatus paymentStatus (BigDecimal paymentValue, BigDecimal originalAmount) {
        return switch (paymentValue.compareTo(originalAmount)) {
            case -1 -> PaymentStatus.PARTIAL;
            case 0 -> PaymentStatus.TOTAL;
            case 1 -> PaymentStatus.EXCESS;
            default -> throw new IllegalStateException("Unexpected value: " + paymentValue);
        };
    }
}
