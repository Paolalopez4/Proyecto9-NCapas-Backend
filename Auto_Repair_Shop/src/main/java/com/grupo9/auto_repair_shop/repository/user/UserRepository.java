package com.grupo9.auto_repair_shop.repository.user;

import com.grupo9.auto_repair_shop.entity.user.User;
import com.grupo9.auto_repair_shop.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByRole(UserRole role, Pageable pageable);

    boolean existsByEmailAndIdNot(String email, UUID id);

    long countByRoleAndActive(UserRole role, Boolean active);
}