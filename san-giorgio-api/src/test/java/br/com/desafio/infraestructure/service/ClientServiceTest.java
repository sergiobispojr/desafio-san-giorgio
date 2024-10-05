package br.com.desafio.infraestructure.service;

import br.com.desafio.domain.exception.EntityNotFoundException;
import br.com.desafio.infraestructure.entity.ClientEntity;
import br.com.desafio.infraestructure.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static br.com.desafio.util.Assertions.assertThrowsWithMessage;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void testFindById() {
        String id = "123";
        ClientEntity expectedClient = new ClientEntity();
        expectedClient.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(expectedClient));

        clientService.findById(id);

        verify(repository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        String invalidId = "321";
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrowsWithMessage(EntityNotFoundException.class,
                () -> clientService.findById(invalidId),
                "Client not found");

        // Assert
        verify(repository, times(1)).findById(invalidId);
    }
}