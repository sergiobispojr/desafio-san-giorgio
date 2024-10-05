package br.com.desafio.domain.usecase.impl;

import br.com.desafio.domain.model.*;
import br.com.desafio.domain.usecase.PaymentQueueUseCase;
import br.com.desafio.infraestructure.entity.PaymentEntity;
import br.com.desafio.infraestructure.entity.ClientEntity;
import br.com.desafio.infraestructure.service.PaymentService;
import br.com.desafio.infraestructure.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmPaymentUseCaseImplTest {

    private ClientEntity clientEntity;
    private PaymentEntity paymentEntity;
    private PaymentEntity paymentEntity2;
    private PaymentEntity paymentEntity3;
    private PaymentModel paymentModel;


    @Mock
    private PaymentService chargeService;
    @Mock
    private ClientService clientService;
    @Mock
    private PaymentQueueUseCase sendToExternalQueue;


    @InjectMocks
    private ConfirmPaymentUseCaseImpl confirmPaymentService;

    @BeforeEach
    void setup() {
        clientEntity = new ClientEntity();
        clientEntity.setId("client-1");

        paymentEntity = new PaymentEntity();
        paymentEntity.setId("payment-1");
        paymentEntity.setOriginalAmount(BigDecimal.valueOf(100.99));

        paymentEntity2 = new PaymentEntity();
        paymentEntity2.setId("payment-2");
        paymentEntity2.setOriginalAmount(BigDecimal.valueOf(5.99));

        paymentEntity3 = new PaymentEntity();
        paymentEntity3.setId("payment-3");
        paymentEntity3.setOriginalAmount(BigDecimal.valueOf(1000.00));

        paymentModel = new PaymentModel(
                clientEntity.getId(),
                List.of(
                        new PaymentItemModel(paymentEntity.getId(), BigDecimal.valueOf(100.99), null),
                        new PaymentItemModel(paymentEntity2.getId(), BigDecimal.valueOf(59.90), null),
                        new PaymentItemModel(paymentEntity3.getId(), BigDecimal.valueOf(100.00), null)
                )
        );
    }

    @Test
    void testStatusPaymentWithTotalExcessAndPartialStatus() {
        doNothing().when(clientService).findById(clientEntity.getId());
        doNothing().when(sendToExternalQueue).enqueuePayment(any(PaymentItemModel.class));

        when(chargeService.getOriginalAmountFromId(paymentEntity.getId()))
                .thenReturn(paymentEntity.getOriginalAmount());

        when(chargeService.getOriginalAmountFromId(paymentEntity2.getId()))
                .thenReturn(paymentEntity2.getOriginalAmount());

        when(chargeService.getOriginalAmountFromId(paymentEntity3.getId()))
                .thenReturn(paymentEntity3.getOriginalAmount());

        PaymentModel result = confirmPaymentService.confirm(paymentModel);

        assertAll("Assert that PaymentModel has a TOTAL, EXCESS and PARTIAL payment status",
                () -> assertTrue(result.paymentItems().stream()
                        .anyMatch(item -> item.paymentStatus() == PaymentStatus.TOTAL)),
                () -> assertTrue(result.paymentItems().stream()
                        .anyMatch(item -> item.paymentStatus() == PaymentStatus.EXCESS)),
                () -> assertTrue(result.paymentItems().stream()
                        .anyMatch(item -> item.paymentStatus() == PaymentStatus.PARTIAL))
        );

        verify(clientService, times(1)).findById(clientEntity.getId());
        verify(chargeService, times(3)).getOriginalAmountFromId(anyString());
        verify(sendToExternalQueue, times(3)).enqueuePayment(any(PaymentItemModel.class));

    }
}
