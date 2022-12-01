package com.socialgallery.gallerybackend.dto.user;

import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * https://ws-pace.tistory.com/72?category=964036
 */

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {
    private String email;
    private String username;

    private String password;

    private String phone;
    private String picture;

    private boolean fromSocial;

    private boolean isLogin;

    @Builder
    public UserRequestDTO(String email, String username, String password, String phone, String picture, boolean fromSocial,
                          boolean isLogin) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.picture = picture;
        this.fromSocial = fromSocial;
        this.isLogin = isLogin;
    }

    public Users toEntity() {
        return Users.builder()
                .email(email)
                .username(username)
                .password(password)
                .phone(phone)
                .picture(picture)
                .fromSocial(fromSocial)
                .isLogin(isLogin)
                .build();
    }
}
