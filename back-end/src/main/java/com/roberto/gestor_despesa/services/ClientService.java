package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.ClientRequestDTO;
import com.roberto.gestor_despesa.dtos.response.ClientResponseDTO;
import com.roberto.gestor_despesa.entities.Client;
import java.util.List;

public interface ClientService {

   public Client save(ClientRequestDTO request);

   public Client update(ClientRequestDTO request, Integer id);

   public List<Client> findAll();

   public ClientResponseDTO findClientById(Integer id);
}
