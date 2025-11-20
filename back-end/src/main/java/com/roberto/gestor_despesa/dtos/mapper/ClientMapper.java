package com.roberto.gestor_despesa.dtos.mapper;


import com.roberto.gestor_despesa.dtos.request.ClientRequestDTO;
import com.roberto.gestor_despesa.dtos.response.ClientResponseDTO;
import com.roberto.gestor_despesa.entities.Client;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    Client map(ClientRequestDTO request);

    ClientResponseDTO map(Client client);
}
