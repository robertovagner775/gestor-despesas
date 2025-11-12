package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.ClientMapper;
import com.roberto.gestor_despesa.dtos.request.ClientRequestDTO;
import com.roberto.gestor_despesa.dtos.response.ClientResponseDTO;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.handler.exceptions.ConflictEntityException;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.services.ClientService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final PasswordEncoder encoder;

    public ClientServiceImpl(ClientRepository repository, ClientMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public Client save(ClientRequestDTO request) {

        if(repository.existsByEmail(request.email()))
            throw new ConflictEntityException("This email: "+ request.email() +" already exists");

        Client client = mapper.map(request);

        client.setPassword(encoder.encode(client.getPassword()));

        return repository.save(client);
    }

    @Override
    public Client update(ClientRequestDTO request, Integer id) {
        return null;
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public ClientResponseDTO findClientById(Integer id) {
        return mapper.map(repository.findById(id).orElseThrow(() -> new NotFoundException(id)));
    }

}
