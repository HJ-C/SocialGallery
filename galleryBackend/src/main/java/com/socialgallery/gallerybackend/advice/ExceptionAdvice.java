package com.socialgallery.gallerybackend.advice;

import com.socialgallery.gallerybackend.advice.exception.*;
import com.socialgallery.gallerybackend.model.response.CommonResult;
import com.socialgallery.gallerybackend.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/*
 * @Reference https://ws-pace.tistory.com/71?category=964036
 */

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    /***
     * -9999
     * default Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // Http Response Code를 500으로 설정한다.
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        log.info(String.valueOf(e));
        return responseService.getFailResult(
                Integer.parseInt(getMessage("unKnown.code")), getMessage("unKnown.msg")
        ); // 실패 시 응답 데이터 반환
    }

    /***
     * -1000
     * 유저를 찾지 못했을 때 발생하는 예외
     */
    @ExceptionHandler(UserNotFoundCException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult userNotFoundException(HttpServletRequest request, UserNotFoundCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    /***
     * -1001
     * 유저 이메일 로그인 실패 시 발생시키는 예외
     */
    @ExceptionHandler(EmailLoginFailedCException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected CommonResult emailLoginFailedException(HttpServletRequest request, EmailLoginFailedCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("emailLoginFailed.code")), getMessage("emailLoginFailed.msg")
        );
    }

    /***
     * -1002
     * 회원 가입 시 이미 로그인 된 이메일인 경우 발생 시키는 예외
     */
    @ExceptionHandler(EmailSignUpFailedCException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult emailSignupFailedException(HttpServletRequest request, EmailSignUpFailedCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("emailSignupFailed.code")), getMessage("emailSignupFailed.msg")
        );
    }

    /**
     * -1003
     * 전달한 Jwt 이 정상적이지 않은 경우 발생 시키는 예외
     */
    @ExceptionHandler(AuthenticationEntryPointCException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult authenticationEntrypointException(HttpServletRequest request, AuthenticationEntryPointCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("authenticationEntrypoint.code")), getMessage("authenticationEntrypoint.msg")
        );
    }

    /**
     * -1004
     * 권한이 없는 리소스를 요청한 경우 발생 시키는 예외
     */
    @ExceptionHandler(AccessDeniedCException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult accessDeniedException(HttpServletRequest request, AccessDeniedCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("accessDenied.code")), getMessage("accessDenied.msg")
        );
    }

    /**
     * -1005
     * refresh token 에러시 발생 시키는 에러
     */
    @ExceptionHandler(RefreshTokenCException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult refreshTokenException(HttpServletRequest request, RefreshTokenCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("refreshTokenInValid.code")), getMessage("refreshTokenInValid.msg")
        );
    }

    /**
     * -1006
     * 액세스 토큰 만료시 발생하는 에러
     */
    @ExceptionHandler(ExpiredAccessTokenCException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected CommonResult expiredAccessTokenException(HttpServletRequest request, ExpiredAccessTokenCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("expiredAccessToken.code")), getMessage("expiredAccessToken.msg")
        );
    }

    /**
     * -1007
     * 게시글을 찾지 못할시 발생하는 에러
     */

    @ExceptionHandler(PostNotFoundCException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult postNotFoundException(HttpServletRequest request, PostNotFoundCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("postNotFound.code")), getMessage("postNotFound.msg")
        );
    }

    /**
     * -1008
     * 파일을 찾지 못할시 발생하는 에러
     */
    @ExceptionHandler(ImageNotFoundCException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult imageNotFoundException(HttpServletRequest request, ImageNotFoundCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("imageNotFound.code")), getMessage("imageNotFound.msg")
        );
    }

    /**
     * -1009
     * 댓글을 찾지 못할시 발생하는 에러
     */
    @ExceptionHandler(CommentNotFoundCException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected CommonResult commentNotFoundCException(HttpServletRequest request, CommentNotFoundCException e) {
        return responseService.getFailResult(
                Integer.parseInt(getMessage("commentNotFound.code")), getMessage("commentNotFound.msg")
        );
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
