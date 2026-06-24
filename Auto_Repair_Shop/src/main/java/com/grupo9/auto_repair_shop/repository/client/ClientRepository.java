package com.grupo9.auto_repair_shop.repository.client;

import com.grupo9.auto_repair_shop.entity.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository <Client, UUID> {

    Optional<Client> findByUserId(UUID userId);
    
    Page<Client> findAll(Pageable pageable);

    boolean existsByUserId(UUID userId);
}