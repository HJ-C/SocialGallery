package com.socialgallery.gallerybackend.advice.exception;

public class ExpiredAccessTokenCException extends RuntimeException {
    public ExpiredAccessTokenCException() {
        super();
    }

    public ExpiredAccessTokenCException(String message) {
        super(message);
    }

    public ExpiredAccessTokenCException(String message, Throwable cause) {
        super(message, cause);
    }
}
