package com.grupo9.auto_repair_shop.repository.part;

import com.grupo9.auto_repair_shop.entity.part.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartRepository extends JpaRepository<Part, UUID> {

    boolean existsBySku(String sku);

    Page<Part> findAll(Pageable pageable);
}
