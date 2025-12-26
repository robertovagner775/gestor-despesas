package com.roberto.gestor_despesa.dtos.mapper;


import com.roberto.gestor_despesa.dtos.request.ClientRequest;
import com.roberto.gestor_despesa.dtos.response.ClientResponse;
import com.roberto.gestor_despesa.entities.Client;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", constant = "false")
    Client map(ClientRequest request);

    ClientResponse map(Client client);
}
