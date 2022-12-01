package com.socialgallery.gallerybackend.controller.comment;

import com.socialgallery.gallerybackend.dto.comment.CommentRequestDTO;
import com.socialgallery.gallerybackend.dto.comment.CommentResponseDTO;
import com.socialgallery.gallerybackend.model.response.ListResult;
import com.socialgallery.gallerybackend.model.response.SingleResult;
import com.socialgallery.gallerybackend.service.comment.CommentService;
import com.socialgallery.gallerybackend.service.response.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = {"5. comment"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    private final ResponseService responseService;

    @ApiOperation(value = "등록", notes = "댓글을 등록합니다.")
    @PostMapping("/register")
    public SingleResult<Long> register(
            @ApiParam(value = "댓글 등록 DTO", required = true)
            @RequestBody CommentRequestDTO commentRequestDTO, HttpServletRequest request) {
        Long cid = commentService.commentSave(Long.valueOf(commentRequestDTO.getPid()), commentRequestDTO, request);
        log.info("=================================================================================");
        log.info("COMMENTREQUESTDTO'POST : " + commentRequestDTO.getPid());
        log.info("=================================================================================");
        log.info("COMMENTREQUESTDTO'USER : " + commentRequestDTO.getUsers());
        log.info("=================================================================================");
        return responseService.getSingleResult(cid);
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글을 수정합니다.")
    @PutMapping("/{pid}/update={cid}")
    public SingleResult<Long> update(
            @ApiParam(value = "게시글 pk", required = true)
            @PathVariable("pid") String pid, @PathVariable("cid") String cid,
            @RequestBody CommentRequestDTO commentRequestDTO, HttpServletRequest request) throws Exception {

        return responseService.getSingleResult(
                commentService.update(Long.valueOf(pid), Long.valueOf(cid), commentRequestDTO, request)
        );

    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글을 삭제합니다.")
    @DeleteMapping("/delete/{cid}")
    public SingleResult<Long> delete(
            @ApiParam(value = "게시글pk", required = true)
            @PathVariable("cid") String cid,
            HttpServletRequest request) throws Exception {
        log.info("===============================================");
        log.info("CID : " + cid);
        log.info("===============================================");
        return responseService.getSingleResult(commentService.delete(Long.valueOf(cid), request));
    }
    @ApiOperation(value = "댓글 목록", notes = "댓글을 가져옵니다.")
    @GetMapping("/{pid}")
    public ListResult<CommentResponseDTO> getList(
            @ApiParam(value = "게시글 pk", required = true)
            @PathVariable("pid") String pid) throws Exception {

        List<CommentResponseDTO> commentResponseDTOList = commentService.getListOfComment(Long.valueOf(pid));

        return responseService.getListResult(commentResponseDTOList);
    }

    @GetMapping("/all")
    public ListResult<CommentResponseDTO> getAll() {
        List<CommentResponseDTO> commentResponseDTOList = commentService.getAllOfComment();

        return responseService.getListResult(commentResponseDTOList);
    }

}
