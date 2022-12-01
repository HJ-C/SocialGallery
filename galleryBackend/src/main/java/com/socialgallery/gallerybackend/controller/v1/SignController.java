package com.socialgallery.gallerybackend.controller.v1;

import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.dto.jwt.TokenRequestDTO;
import com.socialgallery.gallerybackend.dto.sign.UserLoginRequestDTO;
import com.socialgallery.gallerybackend.dto.sign.UserSignUpRequestDTO;
import com.socialgallery.gallerybackend.model.response.SingleResult;
import com.socialgallery.gallerybackend.service.response.ResponseService;
import com.socialgallery.gallerybackend.service.security.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 * 로그인, 회원가입, 재발급 요청은 모두 RequestBody로 이뤄진다.
 * 검증 로직은 SignService에서 한다.
 * 응답 부분에서 refresh Token은 안보이게 조정 필요
 */

@Api(tags = "1. SignUp / Login / Logout")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/v1")
public class SignController {

    private final SignService signService;
    private final ResponseService responseService;

    @ApiOperation(value = "로그인", notes = "이메일로 로그인을 합니다.")
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<TokenDTO> login(
            @ApiParam(value = "로그인 요청 DTO", required = true)
            @RequestBody UserLoginRequestDTO userLoginRequestDTO,
            HttpServletResponse response) {

        TokenDTO tokenDTO = signService.login(userLoginRequestDTO);
        log.info("LOGIN 요청, Access 토큰 발행 : " + tokenDTO.getAccessToken());
        log.info("LOGIN 요청, Refresh 토큰 발행 : " + tokenDTO.getRefreshToken());
        response.setHeader("Authorization", "Bearer " + tokenDTO.getAccessToken());
        return responseService.getSingleResult(tokenDTO);
    }

    @ApiOperation(value = "회원가입", notes = "회원가입을 합니다.")
    @PostMapping(value = "/signUp", produces = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<Long> signUp(
            @ApiParam(value = "회원 가입 요청 DTO", required = true)
            @RequestBody UserSignUpRequestDTO userSignUpRequestDTO) {

        Long signUpId = signService.signUp(userSignUpRequestDTO);
        log.info("SIGNUP 요청, user pk : " + signUpId);
        return responseService.getSingleResult(signUpId);
    }

    @ApiOperation(value = "액세스, 리프레시 토큰 재발급",
                  notes = "액세스 토큰 만료시 회원 검증 후 리프레시 토큰을 검증해서 액세스 토큰과 리프레스 토큰을 재발급합니다." )
    @PostMapping("/reissue")
    public SingleResult<TokenDTO> reissue(
            @ApiParam(value = "토큰 재발급 요청 DTO", required = true)
            @RequestBody TokenRequestDTO tokenRequestDTO) {
        return responseService.getSingleResult(signService.reissue(tokenRequestDTO));
    }

    @ApiOperation(value = "로그아웃",
                  notes = "리프레시토큰을 지움으로서 로그아웃을 합니다.")
    @GetMapping("/logout")
    public SingleResult<Long> logout(
            @ApiParam(value = "로그아웃", required = true)
            @RequestParam String uid
            ) {
        return responseService.getSingleResult(signService.logout(Long.parseLong(uid)));
    }
}
