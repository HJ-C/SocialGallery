package com.socialgallery.gallerybackend.controller.v1;

import com.socialgallery.gallerybackend.advice.exception.ResourceNotFoundException;
import com.socialgallery.gallerybackend.config.security.CurrentUser;
import com.socialgallery.gallerybackend.dto.oauth.UserPrincipal;
import com.socialgallery.gallerybackend.dto.user.UserRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.model.response.CommonResult;
import com.socialgallery.gallerybackend.model.response.ListResult;
import com.socialgallery.gallerybackend.model.response.SingleResult;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.response.ResponseService;
import com.socialgallery.gallerybackend.service.user.UsersService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/*
 * @Reference https://ws-pace.tistory.com/68?category=964036
 * 요청을 받을때는 UserResponseDTO
 * 요청을 보낼때는 UserRequestDTO
 * 검색,수정,삭제 등을 하는데 토큰이 꼭 필요한지 생각해 봐야함
 * == 필요없을 것 같다. 토큰 삭제 예정
 * mapping url 통일 해야 할듯.
 * @RequestParam을 Body로 받도록 변경 예정
 */
@Api(tags = {"2. Users"})   // 제목 역할
@Controller
@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {


    private final UsersService usersService;

    private final UserRepository userRepository;

    private final ResponseService responseService;


    @GetMapping("/index")
    public String index() {
        return "Hello!";
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원 목록을 조회합니다.")
    @GetMapping("api/users/findAll")
    public ListResult<UserResponseDTO> findAllUser() {
        return responseService.getListResult(usersService.findAllUser());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원 정보를 수정합니다.")
    @PutMapping("/api/users/userUpdate")
    public SingleResult<Long> update(@ApiParam(value = "회원 ID", readOnly = true) @RequestParam Long id,
                                      @ApiParam(value = "회원 이메일", required = true) @RequestParam String email,
                                      @ApiParam(value = "회원 이름", required = true) @RequestParam String username,
                                      @ApiParam(value = "회원 비밀번호", required = true) @RequestParam String password,
                                      @ApiParam(value = "회원 핸드폰번호", required = false) @RequestParam String phone) {
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .email(email)
                .username(username)
                .password(password)
                .phone(phone)
                .build();

        return responseService.getSingleResult(usersService.update(id, userRequestDTO));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "회원 정보를 삭제합니다.")
    @DeleteMapping("/api/users/userDelete/{id}")
    public CommonResult delete(@ApiParam(value = "회원 아이디", required = true)@PathVariable Long id) {
        usersService.delete(id);
        return responseService.getSuccessResult();
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public Users getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 단건 검색", notes = "id로 회원을 조회합니다.")
    @GetMapping("/findUserById/{id}")
    public SingleResult<UserResponseDTO> findUserById(
            @ApiParam(value = "회원 ID", required = true) @PathVariable Long id
            //@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
            ) {
        return responseService
                .getSingleResult(usersService.findById(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 검색 (이름)", notes = "이름으로 회원을 검색합니다.")
    @GetMapping("/findUserByName/{username}")
    public SingleResult<UserResponseDTO> findUserByName(@ApiParam(value = "회원 이름", required = true) @PathVariable String username
//                                              @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
                                              )
    {
        return responseService.getSingleResult(usersService.findByUsername(username));
    }

    @ApiOperation(value = "회원 검색(이메일)", notes = "이메일로 회원을 검색합니다.")
    @GetMapping("/findUserByEmail/{email}")
    public SingleResult<UserResponseDTO> findUserByEmail(@ApiParam(value = "회원 이메일", required = true)@PathVariable String email
                                                         //@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang
                                                         ) {
        return responseService.getSingleResult(usersService.findByEmail(email));
    }

//    @ResponseBody
//    @PostMapping(value = "api/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> signIn(@RequestBody Users users) {
//        System.out.println("Sign In");
//        System.out.println(users);
//        userService.signIn(users.getEmail(), users.getPassword());
//        String loginUsers = userService.signIn(users.getEmail(), users.getPassword());
//        String result = jwtTokenProvider.getUsername(loginUsers);
//            return ResponseEntity.ok()
//                    .body(result);
//    }

//    @ResponseBody
//    @PostMapping(value = "api/signUp", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> signUp(@RequestBody UserDTO dto) {
//
//        System.out.println("Sign Up");
//        System.out.println(dto);
//        Users registeredUsers = userService.signUp(dto);
//
//        UserDTO userDTO = userService.entitiesToDTO(registeredUsers);
//
//        return ResponseEntity.ok().body(userDTO);
//    }

}
