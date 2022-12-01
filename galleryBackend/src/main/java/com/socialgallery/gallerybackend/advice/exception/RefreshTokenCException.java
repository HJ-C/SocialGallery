package com.socialgallery.gallerybackend.advice.exception;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 */
public class RefreshTokenCException extends RuntimeException{
    public RefreshTokenCException() {
        super();
    }

    public RefreshTokenCException(String message) {
        super(message);
    }

    public RefreshTokenCException(String message, Throwable cause) {
        super(message, cause);
    }
}
