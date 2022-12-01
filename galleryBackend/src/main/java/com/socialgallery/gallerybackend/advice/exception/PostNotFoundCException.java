package com.socialgallery.gallerybackend.advice.exception;

public class PostNotFoundCException extends RuntimeException{

    public PostNotFoundCException() {
        super();
    }

    public PostNotFoundCException(String message) {
        super(message);
    }

    public PostNotFoundCException(String message, Throwable cause) {
        super(message, cause);
    }
}
