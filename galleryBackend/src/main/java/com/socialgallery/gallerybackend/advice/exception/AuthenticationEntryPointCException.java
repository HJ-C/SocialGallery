package com.socialgallery.gallerybackend.advice.exception;

public class AuthenticationEntryPointCException extends RuntimeException{
    public AuthenticationEntryPointCException() {
        super();
    }

    public AuthenticationEntryPointCException(String message) {
        super(message);
    }

    public AuthenticationEntryPointCException(String message, Throwable cause) {
        super(message, cause);
    }
}
