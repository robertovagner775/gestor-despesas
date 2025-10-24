package com.roberto.gestor_despesa.services.impl;

import com.roberto.gestor_despesa.dtos.mapper.ClientMapper;
import com.roberto.gestor_despesa.dtos.request.ClientRequestDTO;
import com.roberto.gestor_despesa.dtos.response.ClientResponseDTO;
import com.roberto.gestor_despesa.entities.Client;
import com.roberto.gestor_despesa.handler.exceptions.NotFoundException;
import com.roberto.gestor_despesa.repository.ClientRepository;
import com.roberto.gestor_despesa.services.ClientService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    private final ClientMapper mapper;

    public ClientServiceImpl(ClientRepository repository, ClientMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Client save(ClientRequestDTO request) {

        // TODO: PRECISA ADCICIONAR A CONFIRMAçao de email

        // TODO: VALIDAR O CPF

        // TODO: VALIDAR E VERIFICAR SE O E-MAIL OU O CPF Já foi cadastrado

        return repository.save(mapper.map(request));
    }

    @Override
    public Client update(ClientRequestDTO request) {
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
