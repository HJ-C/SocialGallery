package com.socialgallery.gallerybackend.config.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * @Reference https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
 * 현재 인증된 사용자 주체를 컨트롤러에 주입하는 데 사용할 수 있는 메타 주석입니다.
 */

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
