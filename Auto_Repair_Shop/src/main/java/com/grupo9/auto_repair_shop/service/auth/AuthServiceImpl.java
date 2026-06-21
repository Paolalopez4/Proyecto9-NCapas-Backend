package com.grupo9.auto_repair_shop.service.auth;

import com.grupo9.auto_repair_shop.dto.request.auth.ChangePasswordRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.LoginRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.RegisterRequest;
import com.grupo9.auto_repair_shop.dto.response.auth.AuthUserResponse;
import com.grupo9.auto_repair_shop.dto.response.auth.LoginResponse;
import com.grupo9.auto_repair_shop.entity.user.User;
import com.grupo9.auto_repair_shop.enums.UserRole;
import com.grupo9.auto_repair_shop.exception.BusinessRuleException;
import com.grupo9.auto_repair_shop.exception.ConflictException;
import com.grupo9.auto_repair_shop.exception.UnauthorizedException;
import com.grupo9.auto_repair_shop.mapper.auth.AuthMapper;
import com.grupo9.auto_repair_shop.repository.user.UserRepository;
import com.grupo9.auto_repair_shop.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthMapper authMapper;

    private final JwtService jwtService;

    @Override
    public AuthUserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Ya existe un usuario con ese email");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CLIENT)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return authMapper.toAuthUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException("El usuario está desactivado");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user.getEmail());

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(authMapper.toAuthUserResponse(user))
                .build();
    }

    @Override
    public void logout() {
        // JWT es stateless.
        // El frontend o Postman debe eliminar el token localmente.
    }

    @Override
    public AuthUserResponse changePassword(ChangePasswordRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("Usuario no autenticado");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException("El usuario está desactivado");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("La contraseña actual es incorrecta");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new BusinessRuleException("La nueva contraseña no puede ser igual a la actual");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));

        User updatedUser = userRepository.save(user);

        return authMapper.toAuthUserResponse(updatedUser);
    }
}