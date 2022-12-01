package com.socialgallery.gallerybackend.advice.exception;

public class ImageNotFoundCException extends RuntimeException{

    public ImageNotFoundCException() {
        super();
    }

    public ImageNotFoundCException(String message) {
        super(message);
    }

    public ImageNotFoundCException(String message, Throwable cause) {
        super(message, cause);
    }
}
