package com.socialgallery.gallerybackend.dto.jwt;

import lombok.*;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireDate;
}
