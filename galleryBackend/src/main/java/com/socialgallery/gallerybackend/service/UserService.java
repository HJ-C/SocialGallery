//package com.socialgallery.gallerybackend.service;
//
//import com.socialgallery.gallerybackend.dto.UserDTO;
//import com.socialgallery.gallerybackend.entity.user.Users;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Collections;
//import java.util.Map;
//
//public interface UserService {
//
//    Users signUp(UserDTO userDTO);
//
//    String signIn(String email, String password);
//
//    default UserDTO entitiesToDTO(Users users) {
//        UserDTO userDTO = UserDTO.builder()
//                .username(users.getUsername())
//                .email(users.getEmail())
//                .password(users.getPassword())
//                .picture(users.getPicture())
//                .phone(users.getPhone())
//                .regDate(users.getRegDate())
//                .modDate(users.getModDate())
//                .build();
//
//        return userDTO;
//    }
//
//    default Users dtoToEntity(UserDTO userDTO) {
//
//        Users users = Users.builder()
//                .username(userDTO.getUsername())
//                .email(userDTO.getEmail())
//                .password(userDTO.getPassword())
//                .picture(userDTO.getPicture())
//                .phone(userDTO.getPhone())
//                .roles(Collections.singletonList("ROLE_USER"))  // 최초 가입시 USER로 설정
//                .build();
//
//        return users;
//    }
//
//}
