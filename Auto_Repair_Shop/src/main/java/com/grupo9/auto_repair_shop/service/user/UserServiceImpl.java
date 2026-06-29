package com.grupo9.auto_repair_shop.service.user;

import com.grupo9.auto_repair_shop.dto.request.user.UpdateUserRequest;
import com.grupo9.auto_repair_shop.dto.request.user.UserRequest;
import com.grupo9.auto_repair_shop.dto.response.common.PageResponse;
import com.grupo9.auto_repair_shop.dto.response.user.UserResponse;
import com.grupo9.auto_repair_shop.entity.user.User;
import com.grupo9.auto_repair_shop.enums.UserRole;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.ResourceNotFoundException;
import com.grupo9.auto_repair_shop.exception.ValidationException;
import com.grupo9.auto_repair_shop.mapper.user.UserMapper;
import com.grupo9.auto_repair_shop.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("A user with that email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> findAll(UserRole role, int page, int size) {

        if (page < 0) {
            throw new ValidationException("Page cannot be less than zero");
        }

        if (size < 1 || size > 100) {
            throw new ValidationException("Page size must be between 1 and 100");
        }

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<User> users;

        if (role != null) {
            users = userRepository.findByRole(role, pageRequest);
        } else {
            users = userRepository.findAll(pageRequest);
        }

        Page<UserResponse> responsePage = users.map(userMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id
                ));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id
                ));

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new ConflictException("Another user with that email already exists");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse updateActive(UUID id, Boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id
                ));

        user.setActive(active);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id
                ));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getName() != null) {
            String currentUserEmail = authentication.getName();

            if (user.getEmail().equals(currentUserEmail)) {
                throw new BusinessRuleException("You cannot delete your own user");
            }
        }

        if (user.getRole() == UserRole.ADMIN) {
            long activeAdmins = userRepository.countByRoleAndActive(UserRole.ADMIN, true);

            if (activeAdmins <= 1 && Boolean.TRUE.equals(user.getActive())) {
                throw new BusinessRuleException("The last active administrator cannot be deleted");
            }
        }

        try {
            userRepository.delete(user);
            userRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException(
                    "The user cannot be deleted because it has related information"
            );
        }
    }
}