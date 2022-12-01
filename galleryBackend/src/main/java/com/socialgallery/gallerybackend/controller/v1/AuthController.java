//package com.socialgallery.gallerybackend.controller.v1;
//
//import com.socialgallery.gallerybackend.advice.exception.BadRequestException;
//import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
//import com.socialgallery.gallerybackend.config.security.JwtProvider;
//import com.socialgallery.gallerybackend.dto.sign.UserLoginRequestDTO;
//import com.socialgallery.gallerybackend.dto.sign.UserSignUpRequestDTO;
//import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
//import com.socialgallery.gallerybackend.entity.user.AuthProvider;
//import com.socialgallery.gallerybackend.entity.user.Users;
//import com.socialgallery.gallerybackend.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;
//import java.io.IOException;
//
///**
// * 삭제 예정
// */
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtProvider jwtProvider;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        userLoginRequestDTO.getEmail(),
//                        userLoginRequestDTO.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = jwtProvider.oauth2CreateToken(authentication);
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody UserSignUpRequestDTO userSignUpRequestDTO) {
//        if(userRepository.existsByEmail(userSignUpRequestDTO.getEmail())) {
//            throw new BadRequestException("Email address already in use.");
//        }
//
//        // Creating user's account
//        Users users = new Users.UsersBuilder().email(userSignUpRequestDTO.getEmail())
//                .username(userSignUpRequestDTO.getUsername())
//                .password(userSignUpRequestDTO.getPassword())
//                .authProvider(AuthProvider.local)
//                .build();
//        users.(userSignUpRequestDTO.getUsername());
//        users.setEmail(userSignUpRequestDTO.getEmail());
//        users.setPassword(userSignUpRequestDTO.getPassword());
//        users.setProvider(AuthProvider.local);
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        User result = userRepository.save(user);
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/user/me")
//                .buildAndExpand(result.getId()).toUri();
//
//        return ResponseEntity.created(location)
//                .body(new ApiResponse(true, "User registered successfully@"));
//    }
//}
