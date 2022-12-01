package com.socialgallery.gallerybackend.controller.exception;

import com.socialgallery.gallerybackend.advice.exception.AccessDeniedCException;
import com.socialgallery.gallerybackend.advice.exception.AuthenticationEntryPointCException;
import com.socialgallery.gallerybackend.model.response.CommonResult;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 */

@Api(tags = {"3. Exception"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entryPoint")
    public CommonResult entrypointException() {
        throw new AuthenticationEntryPointCException();
    }

    @GetMapping("/accessDenied")
    public CommonResult accessDeniedException() {
        throw new AccessDeniedCException("");
    }
}
