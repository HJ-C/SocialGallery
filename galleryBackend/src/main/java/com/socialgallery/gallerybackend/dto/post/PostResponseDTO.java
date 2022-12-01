package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.dto.comment.CommentResponseDTO;
import com.socialgallery.gallerybackend.entity.post.Category;
import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@Getter
public class PostResponseDTO {

    private final Long pid;

    private final String title;

    private final String content;

    private final String username;

    private final int hits;

    private final int reviewCnt;

    private final int likeCnt;

    private final Category category;

    private final List<Long> fileId;  // 첨부 파일 id 목록

    private final List<CommentResponseDTO> comments;

    public PostResponseDTO(Post post, List<Long> fileId) {
        this.pid = post.getPid();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUsers().getUsername();
        this.hits = post.getHits();
        this.reviewCnt = post.getReviewCnt();
        this.likeCnt = post.getLikeCnt();
        this.category = post.getCategory();
        this.fileId = fileId;
        this.comments = post.getComments().stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }


}