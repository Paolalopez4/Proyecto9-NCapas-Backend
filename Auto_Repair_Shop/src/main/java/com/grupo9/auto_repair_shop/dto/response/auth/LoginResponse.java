package com.grupo9.auto_repair_shop.dto.response.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;

    private String tokenType;

    private AuthUserResponse user;
}