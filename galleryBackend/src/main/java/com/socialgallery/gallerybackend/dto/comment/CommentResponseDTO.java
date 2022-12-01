package com.socialgallery.gallerybackend.dto.comment;

import com.socialgallery.gallerybackend.entity.comment.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDTO {

    private final Long cid;

    private final String comment;

    private final String username;

    private final Long pid;

    private final Long uid;

    public CommentResponseDTO(Comment comment) {
        this.cid = comment.getCid();
        this.comment = comment.getComment();
        this.username = comment.getUsers().getUsername();
        this.pid = comment.getPost().getPid();
        this.uid = comment.getUsers().getId();

    }
}
