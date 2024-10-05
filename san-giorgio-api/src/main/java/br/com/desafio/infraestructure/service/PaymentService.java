package br.com.desafio.infraestructure.service;

import br.com.desafio.domain.exception.EntityNotFoundException;
import br.com.desafio.infraestructure.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public BigDecimal getOriginalAmountFromId(String id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Payment not found"))
                .getOriginalAmount();
    }
}
