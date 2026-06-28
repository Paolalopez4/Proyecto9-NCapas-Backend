package com.grupo9.auto_repair_shop.service.client;

import com.grupo9.auto_repair_shop.dto.request.client.ClientRequest;
import com.grupo9.auto_repair_shop.dto.request.client.UpdateClientRequest;
import com.grupo9.auto_repair_shop.dto.response.client.ClientResponse;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.entity.client.Client;
import com.grupo9.auto_repair_shop.entity.user.User;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.mapper.client.ClientMapper;
import com.grupo9.auto_repair_shop.repository.client.ClientRepository;
import com.grupo9.auto_repair_shop.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                        "Usuario no encontrado con id: " + request.getUserId()
                ));

        if (!user.getActive()) {
            throw new ResourceNotFoundException(
                    "El usuario debe estar activo para crear un perfil de cliente"
            );
        }

        if (clientRepository.existsByUserId(request.getUserId())) {
            throw new ConflictException(
                    "Ya existe un perfil de cliente para este usuario"
            );
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
                        "Cliente no encontrado con id: " + id
                ));
        return clientMapper.toResponse(client);
    }

    @Override
    public PageResponse<ClientResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<Client> clientPage = clientRepository.findAll(pageable);
        Page<ClientResponse> responsePage = clientPage.map(clientMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public ClientResponse update(UUID id, UpdateClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con id: " + id
                ));

        client.setPhone(request.getPhone());
        client.setAddress(request.getAddress());

        Client updated = clientRepository.save(client);
        return clientMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no encontrado con id: " + id
                ));

        try {
            clientRepository.delete(client);
            clientRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException(
                    "No se puede eliminar el cliente porque tiene información relacionada"
            );
        }
    }
}
