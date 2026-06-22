package com.grupo9.auto_repair_shop.repository.branch;

import com.grupo9.auto_repair_shop.entity.branch.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {

    boolean existsByName(String name);

    boolean existsByAddress(String address);

    boolean existsByPhone(String phone);

    boolean existsByNameAndIdNot(String name, UUID id);

    boolean existsByAddressAndIdNot(String address, UUID id);

    boolean existsByPhoneAndIdNot(String phone, UUID id);

    Page<Branch> findByActive(Boolean active, Pageable pageable);
}