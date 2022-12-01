package com.socialgallery.gallerybackend.service.user;

import com.socialgallery.gallerybackend.advice.exception.EmailLoginFailedCException;
import com.socialgallery.gallerybackend.advice.exception.EmailSignUpFailedCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.dto.sign.UserLoginResponseDTO;
import com.socialgallery.gallerybackend.dto.sign.UserSignUpRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @Reference https://ws-pace.tistory.com/72?category=964036
 * GET 요청에는 UserResponseDTO를 반환
 * POST PUT 요청에는 UserRequestDTO를 반환
 */

@Service
@AllArgsConstructor
public class UsersService {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserLoginResponseDTO login(String email, String password) {
        Users users = userRepository.findByEmail(email).orElseThrow(EmailLoginFailedCException::new);
        if (!passwordEncoder.matches(password, users.getPassword()))
            throw new EmailLoginFailedCException();
        return new UserLoginResponseDTO(users);
    }

    @Transactional
    public Long signUp(UserSignUpRequestDTO userSignUpDTO) {
        if (userRepository.findByEmail(userSignUpDTO.getEmail()).orElse(null) == null)
            return userRepository.save(userSignUpDTO.toEntity()).getId();
        else throw new EmailSignUpFailedCException();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(UserNotFoundCException::new);
        return new UserResponseDTO(users);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundCException::new);
        return new UserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByUsername(String username) {
        Users users = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundCException::new);
        return new UserResponseDTO(users);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long id, UserRequestDTO userRequestDTO) {
        Users modifiedUser = userRepository
                .findById(id).orElseThrow(UserNotFoundCException::new);
        modifiedUser.updateUsername(userRequestDTO.getUsername());
        return id;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
