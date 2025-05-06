package com.client_db.webapp.service;

import com.client_db.webapp.models.Client;
import com.client_db.webapp.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> searchClients(String keyword) {
        return clientRepository.searchByKeyword(keyword);
    }
}