package com.socialgallery.gallerybackend.dto.user;

import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Getter;

/*
 * https://ws-pace.tistory.com/72?category=964036
 */

@Getter
public class UserResponseDTO {
    private final Long id;
    private final String email;
    private final String username;
    private final String phone;
    private final String picture;
    private final Boolean isLogin;

    public UserResponseDTO(Users users) {
        this.id = users.getId();
        this.email = users.getEmail();
        this.username = users.getUsername();
        this.phone = users.getPhone();
        this.picture = users.getPicture();
        this.isLogin = users.isLogin();
    }
}
