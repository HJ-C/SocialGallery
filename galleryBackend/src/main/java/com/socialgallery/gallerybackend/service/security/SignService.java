package com.socialgallery.gallerybackend.service.security;

import antlr.Token;
import com.socialgallery.gallerybackend.advice.exception.EmailLoginFailedCException;
import com.socialgallery.gallerybackend.advice.exception.EmailSignUpFailedCException;
import com.socialgallery.gallerybackend.advice.exception.RefreshTokenCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.jwt.TokenDTO;
import com.socialgallery.gallerybackend.dto.jwt.TokenRequestDTO;
import com.socialgallery.gallerybackend.dto.sign.UserLoginRequestDTO;
import com.socialgallery.gallerybackend.dto.sign.UserSignUpRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpRequest;
import java.util.Optional;


/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 * *********************************************************
 * LOGIN
 * 유저가 이메일, 패스워드가 담긴 UserLoginRequestDto로 로그인을 시도하면
 * - 회원이 존재하는지 확인
 * - DB의 패스워드와 전달한 패스워드 일치여부 확인
 * 위 로직을 통과하면 AccessToken & RefreshToken (TokenDto)을 생성해서 발급해준다
 * 액세스 토큰에 유저의 Pk와 권한을 넣어준다
 * - 이때 Pk값은 외부에서 유저를 특정할 수 없는 값으로 하는것이 좋다.
 * 리프레시 토큰은 리프레시 토큰 저장소에 저장해준다. 유저의 pk값을 키값으로 한다
 * - 추후 액세스 토큰만료시 검증용도로 사용한다
 * **********************************************************
 * SIGNUP
 * UserSignupRequestDto로 회원가입을 요청하는 회원의 아이디와 패스워드값을 받는다.
 * 이미 존재하는 아이디가 아니라면 저장시켜준다.
 * 여기서는 토큰을 발급하지 않는다.
 * ***********************************************************
 * ACCESS TOKEN REISSUE
 * TokenRequestDto을 통해 액세스 토큰 재발급을 요청한다
 * - 리프레시 토큰의 만료를 검증한다. -> 만료시 재로그인 요청
 * - 액세스 토큰으로 Authentication 유저를 찾는다. authentication.getName()은
 * - User객체에서 오버라이드한 메소드로 반환값은 DB에 저장되는 인덱스값이자 ID인 userId이다.
 * - - 이메일같은 값으로 하면 토큰의 클레임에서 볼수 있으므로 유저를 특정할 수 있다. 따라서 외부에서 유저를 특정할 수없는 db의 id값으로 취한다.
 * - 리프레시 토큰 저장소에서 유저의 pk값으로 리프레시 토큰을 찾는다 -> 없다면 재로그인 요청
 * - 전달받은 리프레시 토큰과 DB에 저장되있던 리프레시 토큰을 비교한다 -> 다르다면 재로그인 요청
 * - 위 로직이 통과되면 새롭게 액세스 토큰과 리프레시 토큰을 발급한다
 * - - 유저의 리프레시 토큰값을 변경하고, 리프레시 토큰을 다시 저장소에 저장한다.
 * - - DB가 더티체크를 통해 저장되있는 userPk-리프레시 토큰값을 바꿔준다.
 * - 새로 발급한 TokenDto를 반환한다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    @Transactional
    public TokenDTO login(UserLoginRequestDTO userLoginRequestDTO) {

        // 회원 정보 존재하는지 확인
        Users users = userRepository.findByEmail(userLoginRequestDTO.getEmail())
                .orElseThrow(EmailLoginFailedCException::new);

        // isLogin true로 바꾸기

        users.updateIsLogin(true);
        userRepository.save(users);

        // 회원 패스워드 일치 여부 확인
        if (!passwordEncoder.matches(userLoginRequestDTO.getPassword(), users.getPassword()))
            throw new EmailLoginFailedCException();

        // AccessToken, refreshToken 발급
        TokenDTO tokenDTO = jwtProvider.createTokenDto(users.getId(), users.getRoles());

        // refreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(users.getId())
                .token(tokenDTO.getRefreshToken())
                .build();
        refreshTokenJpaRepo.save(refreshToken);
        return tokenDTO;
    }

    @Transactional
    public Long signUp(UserSignUpRequestDTO userSignUpRequestDTO) {
        if (userRepository.findByEmail(userSignUpRequestDTO.getEmail()).isPresent())
            throw new EmailSignUpFailedCException();
        return userRepository.save(userSignUpRequestDTO.toEntity(passwordEncoder)).getId();
    }

    @Transactional
    public TokenDTO reissue(TokenRequestDTO tokenRequestDTO) {
        log.info("=============================================================");
        log.info("REISSUE START");
        log.info("=============================================================");

        // 만료된 refresh token에러
        if (!jwtProvider.validationToken(tokenRequestDTO.getRefreshToken())) {
            throw new RefreshTokenCException();
        }

        // Access Token에서 Username(pk)가져오기
        String accessToken = tokenRequestDTO.getAccessToken();
        log.info("BEFORE ACCESSTOKEN : " + accessToken);
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        log.info("BEFORE AUTHENTICATION: " + authentication.getName());

        // user pk(id)로 유저 검색/ repo에 저장된 refresh token이 없음
        Users users = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundCException::new);
        log.info("SEARCH USERS : " + users);
        RefreshToken refreshToken = refreshTokenJpaRepo.findByKey(users.getId())
                .orElseThrow(RefreshTokenCException::new);
        log.info("SEARCH REFRESHTOKEN : " + refreshToken);

        // refresh token 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDTO.getRefreshToken()))
            throw new RefreshTokenCException();

        // AccessToken 토큰 재발급, RefreshToken 토큰 저장
        TokenDTO newCreatedToken = jwtProvider.createTokenDto(users.getId(), users.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        refreshTokenJpaRepo.save(updateRefreshToken);

        log.info("=============================================================");
        log.info("REISSUE SUCCESS");
        log.info("NEW CREATED TOKEN : " + newCreatedToken.getAccessToken());
        log.info("UPDATE REFRESH TOKEN : " + updateRefreshToken.getToken());
        log.info("NEW AUTHENTICATION : " + jwtProvider.getAuthentication(newCreatedToken.getAccessToken()));
        log.info("=============================================================");
        return newCreatedToken;
    }

    @Transactional
    public Long logout(Long id) {
        // 회원 정보 존재하는지 확인
        Users users = userRepository.findById(id)
                .orElseThrow(UserNotFoundCException::new);

        users.updateIsLogin(false);
        userRepository.save(users);
        log.info("SEARCH USERS : " + users);

        // 회원 정보로 refresh token 찾기
        RefreshToken refreshToken = refreshTokenJpaRepo.findByKey(users.getId())
                .orElseThrow(RefreshTokenCException::new);
        log.info("SEARCH REFRESHTOKEN : " + refreshToken.getToken());

        // 리프레시 토큰 삭제
        refreshTokenJpaRepo.delete(refreshToken);

        return refreshToken.getKey();
    }
}
