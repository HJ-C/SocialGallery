package com.socialgallery.gallerybackend.dto.sign;

import com.socialgallery.gallerybackend.entity.user.AuthProvider;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

/**
 * @Reference https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
 */

@NoArgsConstructor
@Getter
public class OAuth2DTO {
    private String email;
    private String name;
    private String picture;

    private AuthProvider authProvider;

    private String providerId;

    @Builder
    public OAuth2DTO(String email, String name, String picture, AuthProvider authProvider, String providerId) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.authProvider = authProvider;
        this.providerId = providerId;
    }

    public Users toEntity(PasswordEncoder passwordEncoder) {
        return Users.builder()
                .email(email)
                .username(name)
                .picture(picture)
                .authProvider(authProvider)
                .providerId(providerId)
                .fromSocial(true)
                .password(passwordEncoder.encode("1111"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
