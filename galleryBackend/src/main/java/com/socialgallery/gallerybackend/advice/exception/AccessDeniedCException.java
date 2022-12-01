package com.socialgallery.gallerybackend.advice.exception;

/*
 * @Reference https://github.com/choiwoonsik/springboot_RestApi_App_Project/blob/main/restApiSpringBootApp/src/main/java/com/restApi/restApiSpringBootApp/advice/exception/CAccessDeniedException.java
 */
public class AccessDeniedCException extends RuntimeException {
    public AccessDeniedCException() {
    }

    public AccessDeniedCException(String message) {
        super(message);
    }

    public AccessDeniedCException(String message, Throwable cause) {
        super(message, cause);
    }
}
