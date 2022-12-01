//package com.socialgallery.gallerybackend.service;
//
//import com.socialgallery.gallerybackend.dto.UserDTO;
//import com.socialgallery.gallerybackend.entity.user.Users;
//import com.socialgallery.gallerybackend.repository.UserRepository;
//import com.socialgallery.gallerybackend.security.JwtTokenProvider;
//import com.socialgallery.gallerybackend.security.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@Log4j2
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public String signIn(String email, String password) {
//
//        Users users = userRepository.findByEmail(email);
//        log.info("signIn 실행");
//
//        if(users == null)
//            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");
//        if (!passwordEncoder.matches(password, users.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//
//        return jwtTokenProvider.createToken(users.getEmail(), users.getRoles());
//    }
//
//
//
//    @Transactional
//    @Override
//    public Users signUp(UserDTO userDTO) {
//        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        Users users = dtoToEntity(userDTO);
//
//        // 이메일이나 네임을 안적었을 때 예외 발생
//        if (users.getEmail() == null || users.getUsername() == null) {
//            throw new RuntimeException("Invalid argument");
//        }
//        final String email = users.getEmail();
//        final String username = users.getUsername();
//
//        // 이메일이 이미 존재할 시 예외 발생
//        if (userRepository.existsByEmail(email)) {
//            log.warn("Email already exists {}", email);
//            throw new RuntimeException("Email already exists");
//        }
//        // 네임이 이미 존재할 시 예외 발생
//        if (userRepository.existsByUsername(username)) {
//            log.warn("Username already exists {}", username);
//            throw new RuntimeException("Username already exists");
//        }
//        log.info(users);
//        return userRepository.save(users);
//    }
//
//}
