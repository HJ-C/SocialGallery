package com.socialgallery.gallerybackend.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_GUEST, ROLE_USER, ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }
}
