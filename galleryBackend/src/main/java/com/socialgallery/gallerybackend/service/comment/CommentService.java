package com.socialgallery.gallerybackend.service.comment;

import com.socialgallery.gallerybackend.advice.exception.CommentNotFoundCException;
import com.socialgallery.gallerybackend.advice.exception.PostNotFoundCException;
import com.socialgallery.gallerybackend.advice.exception.RefreshTokenCException;
import com.socialgallery.gallerybackend.advice.exception.UserNotFoundCException;
import com.socialgallery.gallerybackend.config.security.JwtProvider;
import com.socialgallery.gallerybackend.dto.comment.CommentRequestDTO;
import com.socialgallery.gallerybackend.dto.comment.CommentResponseDTO;
import com.socialgallery.gallerybackend.dto.jwt.TokenRequestDTO;
import com.socialgallery.gallerybackend.dto.user.UserResponseDTO;
import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.security.RefreshToken;
import com.socialgallery.gallerybackend.entity.security.RefreshTokenJpaRepo;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.repository.CommentRepository;
import com.socialgallery.gallerybackend.repository.PostRepository;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.security.SignService;
import com.socialgallery.gallerybackend.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final UsersService usersService;

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;

    private final JwtProvider jwtProvider;

    private final SignService signService;

    @Transactional
    public Long commentSave(Long pid, CommentRequestDTO commentRequestDTO, HttpServletRequest request) {
        // ????????? ?????? ????????????????????? ????????????
        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
        // ??????????????? ????????? ??????????????? / access ?????? ????????? ?????????
        if (checkToken(commentRequestDTO.getUsers().getId(), request)) {
            // ?????? ?????? post ???????????? ????????????
            Comment entity = commentRequestDTO.toEntity();
            entity.setPost(post);
            // ??????
            commentRepository.save(entity);
            return entity.getCid();
        } throw new IllegalArgumentException("?????? ???????????? ???????????????.");

    }

    // ?????? ??????
    @Transactional
    public Long update(Long pid, Long cid, CommentRequestDTO commentRequestDTO, HttpServletRequest request) {
        // ????????? ?????? ????????????????????? ????????????
        Post post = postRepository.findById(pid).orElseThrow(PostNotFoundCException::new);
        if (checkToken(commentRequestDTO.getUsers().getId(), request)) {
            // ?????? ?????? ????????????
            Optional<Comment> result = commentRepository.findById(cid);
            result.ifPresent(comment -> comment.update(commentRequestDTO.getComment()));
        }
        return cid;
    }

// ?????? ??????
    @Transactional
    public Long delete(Long cid, HttpServletRequest request) {
        // ????????? ?????? ????????????????????? ????????????
        Comment comment = commentRepository.findById(cid).orElseThrow(CommentNotFoundCException::new);

        if (checkToken(comment.getUsers().getId(), request)) {
            commentRepository.deleteByCid(cid);
        }
        return cid;
    }

    // ?????? ????????????
    @Transactional
    public List<CommentResponseDTO> getListOfComment(Long pid) {

        Post post = Post.builder()
                .pid(pid).build();
        List<Comment> result =commentRepository.findByPost(post);

        return result.stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public List<CommentResponseDTO> getAllOfComment() {
        List<Comment> result = commentRepository.findAll();

        return result.stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public boolean checkToken(Long id, HttpServletRequest request) {
        Users users = userRepository.findById(id).orElseThrow(UserNotFoundCException::new);
        UserResponseDTO userPk = usersService.findByUsername(users.getUsername());
        Optional<RefreshToken> refreshToken = refreshTokenJpaRepo.findByKey(userPk.getId());
        String accessToken = jwtProvider.resolveToken(request);
        log.info("ACCESSTOKEN : " + accessToken);
        String rtoken = refreshToken.orElseThrow().getToken();
        log.info("REFRESHTOKEN : " + rtoken);
        if (!jwtProvider.validationToken(rtoken)) {
            request.setAttribute("Authorization", "");
            signService.logout(id);
            throw new RefreshTokenCException();
        }
        if (!jwtProvider.validationToken(accessToken)) {
            TokenRequestDTO tokenRequestDTO = TokenRequestDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(rtoken)
                    .build();
            signService.reissue(tokenRequestDTO);
            return true;
        }
        return true;
    }
}
