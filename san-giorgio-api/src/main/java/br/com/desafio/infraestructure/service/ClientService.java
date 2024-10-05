package br.com.desafio.infraestructure.service;

import br.com.desafio.domain.exception.EntityNotFoundException;
import br.com.desafio.infraestructure.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public void findById(String id) {
        if (repository.findById(id).isEmpty())
                throw new EntityNotFoundException("Client not found");
    }
}
