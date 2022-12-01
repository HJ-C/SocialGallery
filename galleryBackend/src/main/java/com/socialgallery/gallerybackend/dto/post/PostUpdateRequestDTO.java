package com.socialgallery.gallerybackend.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@Getter
@NoArgsConstructor
public class PostUpdateRequestDTO {

    private String title;
    private String content;

    @Builder
    public PostUpdateRequestDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
