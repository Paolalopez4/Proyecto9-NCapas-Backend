package com.grupo9.auto_repair_shop.service.user;

import com.grupo9.auto_repair_shop.dto.request.user.UpdateUserRequest;
import com.grupo9.auto_repair_shop.dto.request.user.UserRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.user.UserResponse;
import com.grupo9.auto_repair_shop.enums.UserRole;

import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest request);

    PageResponse<UserResponse> findAll(UserRole role, int page, int size);

    UserResponse findById(UUID id);

    UserResponse update(UUID id, UpdateUserRequest request);

    UserResponse updateActive(UUID id, Boolean active);

    void delete(UUID id);
}
