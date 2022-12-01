package com.socialgallery.gallerybackend.dto.post;

import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@Getter
public class PostListResponseDTO {

    private Long pid;
    private String username;
    private String title;
    private String content;

    private List<String> filePath = new ArrayList<>();

    private List<Long> thumbnailId = new ArrayList<>();   // 썸네일 Id'

    private List<String > comments = new ArrayList<>();

    public PostListResponseDTO(Post post) {
        List<Image> pathList = post.getImages();
        pathList.forEach(image -> {
            filePath.add(image.getFilePath());
        });
        List<Image> thumbnailList = post.getImages();
        thumbnailList.forEach(image -> {
            thumbnailId.add(image.getIid());
        });
        List<Comment> commentList = post.getComments();
        commentList.forEach(comment -> {
            comments.add(post.getComments().toString());
        });
        this.pid = post.getPid();
        this.username = post.getUsers().getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.filePath = pathList.stream().map(Image::getFilePath).collect(Collectors.toList());
        this.comments = commentList.stream().map(Comment::getComment).collect(Collectors.toList());

        if (!post.getImages().isEmpty())    // 첨부파일 존재 o
                this.thumbnailId = thumbnailList.stream().map(Image::getIid).collect(Collectors.toList());

        else // 첨부파일 존재 x
            this.thumbnailId.add(0, 0L);  // 서버에 저장된 기본 이미지 반환
    }
}
