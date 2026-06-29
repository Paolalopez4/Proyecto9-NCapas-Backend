package com.grupo9.auto_repair_shop.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    public UserDetails loadUserByUsername(String email);

}
