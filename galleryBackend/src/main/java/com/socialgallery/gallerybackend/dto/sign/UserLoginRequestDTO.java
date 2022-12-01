package com.socialgallery.gallerybackend.dto.sign;

import com.socialgallery.gallerybackend.entity.user.AuthProvider;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequestDTO {
    private String email;
    private String password;

    public Users toUser(PasswordEncoder passwordEncoder) {
        return Users.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .authProvider(AuthProvider.local)
                .build();
    }
}
