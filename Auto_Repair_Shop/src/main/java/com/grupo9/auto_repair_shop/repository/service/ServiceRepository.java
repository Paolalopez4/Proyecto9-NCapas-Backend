package com.grupo9.auto_repair_shop.repository.service;

import com.grupo9.auto_repair_shop.entity.service.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    //listar con filtros
    Page<Service> findByNameContainingIgnoreCaseAndActive(String name, Boolean active, Pageable pageable);
    Page<Service> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Service> findByActive(Boolean active, Pageable pageable);

    //validaciones de nombre
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);

    //busqueda por nombre exacto
    Optional<Service> findByNameIgnoreCase(String name);
}