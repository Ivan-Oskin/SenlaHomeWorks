package com.oskin.autoservice.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(("ROLE_") + this.role);
    }
}
