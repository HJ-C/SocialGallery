package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.entity.post.Category;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDTO {

    private Users users;

    private String title;

    private String content;

    private Category category;

    @Builder
    public PostRequestDTO (Users users, String title, String content, Category category) {
        this.users = users;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Post toEntity() {
        return Post.builder()
                .users(users)
                .title(title)
                .content(content)
                .hits(0)
                .reviewCnt(0)
                .likeCnt(0)
                .category(String.valueOf(category))
                .build();
    }
}