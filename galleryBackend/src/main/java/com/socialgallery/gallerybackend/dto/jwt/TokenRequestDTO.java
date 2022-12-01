package com.socialgallery.gallerybackend.dto.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 */

@Getter
@Setter
@NoArgsConstructor
public class TokenRequestDTO {
    String accessToken;
    String refreshToken;

    @Builder
    public TokenRequestDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
