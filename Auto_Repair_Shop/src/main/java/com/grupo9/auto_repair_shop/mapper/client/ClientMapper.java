package com.grupo9.auto_repair_shop.mapper.client;

import com.grupo9.auto_repair_shop.dto.request.client.ClientRequest;
import com.grupo9.auto_repair_shop.dto.response.client.ClientResponse;
import com.grupo9.auto_repair_shop.entity.client.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    ClientResponse toResponse(Client client);

    void updateEntity(ClientRequest request, @MappingTarget Client client);
}
