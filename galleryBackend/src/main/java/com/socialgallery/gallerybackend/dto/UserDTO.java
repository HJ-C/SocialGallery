package com.socialgallery.gallerybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "Id를 입력해주세요.")
    private String username;

    @NotBlank(message = "Email을 입력해주세요.")
    private String email;

    @NotBlank(message = "Password를 입력해주세요.")
    private String password;

    private String picture;

    private String phone;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
