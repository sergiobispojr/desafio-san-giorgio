package br.com.desafio.infraestructure.service;

import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.model.PaymentStatus;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendToSQSTest {

    @Mock
    private SqsClient sqsClient;

    @InjectMocks
    private SendToSQS sendToSQS;

    private static Stream<Arguments> providePaymentItemModels() {
        return Stream.of(
                Arguments.of(new PaymentItemModel(
                        "1",
                        BigDecimal.TEN,
                        PaymentStatus.TOTAL
                )),
                Arguments.of(new PaymentItemModel(
                        "2",
                        BigDecimal.TEN,
                        PaymentStatus.PARTIAL
                )),
                Arguments.of(new PaymentItemModel(
                        "3",
                        BigDecimal.TEN,
                        PaymentStatus.EXCESS
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("providePaymentItemModels")
    void testEnqueuePaymentWithTotal(PaymentItemModel paymentItemModel) {
        sendToSQS.enqueuePayment(paymentItemModel);

        verify(sqsClient).sendMessage(any(SendMessageRequest.class));
    }
}
