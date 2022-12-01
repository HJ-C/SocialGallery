package com.socialgallery.gallerybackend.advice.exception;

public class CommentNotFoundCException extends RuntimeException{

    public CommentNotFoundCException() {
        super();
    }

    public CommentNotFoundCException(String message) {
        super(message);
    }

    public CommentNotFoundCException(String message, Throwable cause) {
        super(message, cause);
    }
}
