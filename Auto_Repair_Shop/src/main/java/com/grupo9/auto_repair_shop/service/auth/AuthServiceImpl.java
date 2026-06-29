package com.grupo9.auto_repair_shop.service.auth;

import com.grupo9.auto_repair_shop.dto.request.auth.ChangePasswordRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.LoginRequest;
import com.grupo9.auto_repair_shop.dto.request.auth.RefreshTokenRequest;
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
            throw new ConflictException("Email is already asigned to another user");
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
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException("User deactivated");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessExpiration())
                .user(authMapper.toAuthUserResponse(user))
                .build();
    }

    @Override
    public void logout() {

        User user = getAuthenticatedUser();

        Long currentVersion = user.getTokenVersion() == null ? 0L : user.getTokenVersion();

        user.setTokenVersion(currentVersion + 1);

        userRepository.save(user);
    }

    @Override
    public AuthUserResponse changePassword(ChangePasswordRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("Not autenticated user");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Not autenticated user"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException("Deactivated user");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("wrong password");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new BusinessRuleException("The new password cannot be the same as the previous one");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));

        Long currentVersion = user.getTokenVersion() == null ? 0L : user.getTokenVersion();
        user.setTokenVersion(currentVersion + 1);

        User updatedUser = userRepository.save(user);

        return authMapper.toAuthUserResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse refresh(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        String email = jwtService.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UnauthorizedException("User deactivated");
        }

        Long tokenVersion = jwtService.extractTokenVersion(refreshToken);
        Long userTokenVersion = user.getTokenVersion() == null ? 0L : user.getTokenVersion();

        if (tokenVersion == null || !userTokenVersion.equals(tokenVersion)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        return LoginResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessExpiration())
                .user(authMapper.toAuthUserResponse(user))
                .build();
    }


    private User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("Not authenticated user");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Not authenticated user"));
    }
}