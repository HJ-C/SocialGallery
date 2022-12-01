package com.socialgallery.gallerybackend.controller;

import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.dto.sign.UserLoginRequestDTO;
import com.socialgallery.gallerybackend.dto.sign.UserSignUpRequestDTO;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.service.post.PostService;
import com.socialgallery.gallerybackend.service.security.SignService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PostControllerTest {

    @Autowired
    private PostService postService;

    @Autowired
    private SignService signService;


    @Test
    public void postTest() {
        UserSignUpRequestDTO userSignUpRequestDTO = UserSignUpRequestDTO.builder()
                .email("user01@social.com")
                .username("user01")
                .password("1111")
                .phone("010-1234-1234")
                .build();

        signService.signUp(userSignUpRequestDTO);

        UserLoginRequestDTO userLoginRequestDTO = UserLoginRequestDTO.builder()
                .email("user01@social.com")
                .password("1111")
                .build();

        signService.login(userLoginRequestDTO);

        Users users = userSignUpRequestDTO.toEntity();

        PostRequestDTO postRequestDTO = PostRequestDTO.builder()
                .users(users)
                .title("테스트")
                .content("테스트 content")
                .build();
    }

}
