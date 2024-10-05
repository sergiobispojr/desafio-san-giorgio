package br.com.desafio.infraestructure.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.desafio.domain.model.PaymentItemModel;
import br.com.desafio.domain.usecase.PaymentQueueUseCase;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class SendToSQS implements PaymentQueueUseCase {
    @Value("${aws.endpoint}")
    private String endpoint;

    @Value("${san-giorgio-api.aws.accountId}")
    private String accountId;

    private static final Logger LOGGER = LoggerFactory.getLogger(SendToSQS.class);

    private final SqsClient sqsClient;

    public SendToSQS(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    @Override
    public void enqueuePayment(PaymentItemModel payment) {
        String queueName = switch (payment.paymentStatus()) {
            case TOTAL -> "total-payment-queue";
            case PARTIAL -> "partial-payment-queue";
            case EXCESS -> "excess-payment-queue";
        };

        sendMessage(payment, queueName);
    }

    private void sendMessage(PaymentItemModel paymentItem, String queueName) {
        String baseURL = endpoint + accountId + "/";
        try {
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(baseURL + queueName)
                    .messageBody(paymentItem.toString())
                    .build();

            sqsClient.sendMessage(request);
            LOGGER.info("Successful sending message [{}] to queue [{}]", paymentItem, queueName);
        } catch (Exception e) {
            LOGGER.error("Error sending message [{}] to queue [{}]", paymentItem, queueName, e);
        }
    }
}
