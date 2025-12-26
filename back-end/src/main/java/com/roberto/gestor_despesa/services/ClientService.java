package com.roberto.gestor_despesa.services;

import com.roberto.gestor_despesa.dtos.request.ClientRequest;
import com.roberto.gestor_despesa.dtos.response.ClientResponse;
import com.roberto.gestor_despesa.entities.Client;
import jakarta.mail.MessagingException;

import java.util.List;

public interface ClientService {

   public Client save(ClientRequest request) throws MessagingException;

   public Client update(ClientRequest request, Integer id);

   public List<Client> findAll();

   public ClientResponse findClientById(Integer id);
}
