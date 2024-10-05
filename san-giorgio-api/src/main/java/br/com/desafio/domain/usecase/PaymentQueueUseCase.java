package br.com.desafio.domain.usecase;


import br.com.desafio.domain.model.PaymentItemModel;

public interface PaymentQueueUseCase {

    void enqueuePayment(PaymentItemModel payment);

}
