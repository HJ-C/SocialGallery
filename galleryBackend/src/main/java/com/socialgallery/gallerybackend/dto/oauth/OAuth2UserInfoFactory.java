package com.socialgallery.gallerybackend.dto.oauth;

import com.socialgallery.gallerybackend.advice.exception.OAuth2AuthenticationProcessingException;
import com.socialgallery.gallerybackend.entity.user.AuthProvider;

import java.util.Map;

/**
 * @Reference https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
 */

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
