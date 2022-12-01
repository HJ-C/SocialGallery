package com.socialgallery.gallerybackend.advice.exception;

import org.springframework.security.core.AuthenticationException;


/**
 * @Reference https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
        super(msg, t);
    }

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
