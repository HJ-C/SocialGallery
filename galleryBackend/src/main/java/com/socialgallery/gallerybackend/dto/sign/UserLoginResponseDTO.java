package com.socialgallery.gallerybackend.dto.sign;

import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserLoginResponseDTO {
    private final Long id;
    private final List<String> roles;
    private final LocalDateTime createDate;

    public UserLoginResponseDTO(Users users) {
        this.id = users.getId();
        this.roles = users.getRoles();
        this.createDate = users.getRegDate();
    }
}
