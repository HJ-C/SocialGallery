package com.socialgallery.gallerybackend.dto.comment;

import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentRequestDTO {

    private String comment;

    private Users users;

    private String pid;


    @Builder
    public CommentRequestDTO(String comment, Users users, String pid) {
        this.comment = comment;
        this.users = users;
        this.pid = pid;
    }

    public Comment toEntity() {
        return Comment.builder()
                .users(users)
                .comment(comment)
                .build();
    }
}