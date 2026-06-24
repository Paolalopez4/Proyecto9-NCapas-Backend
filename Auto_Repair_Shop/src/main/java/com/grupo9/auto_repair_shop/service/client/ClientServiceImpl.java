package com.grupo9.auto_repair_shop.service.client;

import com.grupo9.auto_repair_shop.dto.request.client.ClientRequest;
import com.grupo9.auto_repair_shop.dto.response.client.ClientResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.entity.client.Client;
import com.grupo9.auto_repair_shop.entity.user.User;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.client.ClientMapper;
import com.grupo9.auto_repair_shop.repository.client.ClientRepository;
import com.grupo9.auto_repair_shop.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public ClientResponse create(ClientRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getUserId()));

        if (clientRepository.existsByUserId(request.getUserId())) {
            throw new ConflictException(
                    "A client profile already exists for this user");
        }

        Client client = clientMapper.toEntity(request);
        client.setUser(user);

        Client saved = clientRepository.save(client);
        return clientMapper.toResponse(saved);
    }

    @Override
    public ClientResponse findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + id));
        return clientMapper.toResponse(client);
    }

    @Override
    public PageResponse<ClientResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> clientPage = clientRepository.findAll(pageable);
        Page<ClientResponse> responsePage = clientPage.map(clientMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public ClientResponse update(UUID id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client not found with id: " + id));

        clientMapper.updateEntity(request, client);

        Client updated = clientRepository.save(client);
        return clientMapper.toResponse(updated);
    }
}
