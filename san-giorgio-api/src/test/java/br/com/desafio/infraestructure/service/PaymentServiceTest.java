package br.com.desafio.infraestructure.service;

import br.com.desafio.domain.exception.EntityNotFoundException;
import br.com.desafio.infraestructure.entity.PaymentEntity;
import br.com.desafio.infraestructure.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static br.com.desafio.util.Assertions.assertThrowsWithMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void testFindById() {
        String id = "123";
        PaymentEntity expectedPayment = new PaymentEntity();
        expectedPayment.setId(id);
        expectedPayment.setOriginalAmount(BigDecimal.TEN);

        when(repository.findById(id)).thenReturn(Optional.of(expectedPayment));

        assertEquals(expectedPayment.getOriginalAmount(),
                paymentService.getOriginalAmountFromId(id));

        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        String invalidId = "321";
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrowsWithMessage(EntityNotFoundException.class,
                () -> paymentService.getOriginalAmountFromId(invalidId),
                "Payment not found");

        verify(repository, times(1)).findById(invalidId);
    }
}