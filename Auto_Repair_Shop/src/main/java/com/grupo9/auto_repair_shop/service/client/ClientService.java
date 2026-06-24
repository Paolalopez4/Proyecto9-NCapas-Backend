package com.grupo9.auto_repair_shop.service.client;

import com.grupo9.auto_repair_shop.dto.request.client.ClientRequest;
import com.grupo9.auto_repair_shop.dto.response.client.ClientResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import java.util.UUID;

public interface ClientService {

    ClientResponse create(ClientRequest request);

    ClientResponse findById(UUID id);

    PageResponse<ClientResponse> findAll(int page, int size);

    ClientResponse update(UUID id, ClientRequest request);
}
