package com.socialgallery.gallerybackend.controller.post;

import com.socialgallery.gallerybackend.dto.image.ImageDTO;
import com.socialgallery.gallerybackend.dto.image.ImageResponseDTO;
import com.socialgallery.gallerybackend.dto.image.PostFileVO;
import com.socialgallery.gallerybackend.dto.post.PostListResponseDTO;
import com.socialgallery.gallerybackend.dto.post.PostRequestDTO;
import com.socialgallery.gallerybackend.dto.post.PostResponseDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.post.Category;
import com.socialgallery.gallerybackend.entity.post.Post;
import com.socialgallery.gallerybackend.entity.user.Users;
import com.socialgallery.gallerybackend.model.response.ListResult;
import com.socialgallery.gallerybackend.model.response.SingleResult;
import com.socialgallery.gallerybackend.repository.ImageRepository;
import com.socialgallery.gallerybackend.repository.UserRepository;
import com.socialgallery.gallerybackend.service.image.ImageService;
import com.socialgallery.gallerybackend.service.post.PostService;
import com.socialgallery.gallerybackend.service.response.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@Api(tags = {"4. post"})
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    private final ImageService imageService;

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    private final ResponseService responseService;

    @ApiOperation(value = "업로드", notes = "게시글 업로드를 합니다.")
    @PostMapping(value = "/post/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<Long> post(
            @ApiParam(value = "게시글 등록 DTO", required = true)
            PostFileVO postFileVO,
            HttpServletRequest request) throws Exception{
        log.info("POSTFILEVO : " + postFileVO);
        Optional<Users> users = userRepository.findById(Long.valueOf(postFileVO.getUsersId()));
        log.info("USERS : " + users);
        PostRequestDTO postRequestDTO = PostRequestDTO.builder()
                .users(users.orElseThrow())
                .title(postFileVO.getTitle())
                .content(postFileVO.getContent())
                .category(postFileVO.getCategory())
                .build();

        log.info("POSTREQUESTDTO : " + postRequestDTO);
        Long pid = postService.post(postRequestDTO, postFileVO.getFiles(), request);
        log.info("Pid : " + pid);
        ResponseEntity.ok().body(pid);
        return responseService.getSingleResult(pid);
    }

    @ApiOperation(value = "수정", notes = "게시글을 수정합니다.")
    @PutMapping("/post/modify/{pid}")
    public SingleResult<Long> update(
            @ApiParam(value = "게시글 수정 DTO", required = true)
            @PathVariable("pid") Long pid,
            PostFileVO postFileVO,
            HttpServletRequest request) throws Exception {

        PostRequestDTO postRequestDTO = PostRequestDTO.builder()
                .title(postFileVO.getTitle())
                .content(postFileVO.getContent())
                .build();

        // DB에 저장되어 있는 파일 불러오기
        List<Image> dbImageList = imageRepository.findAllByPostPid(pid);
        // 전달되어온 파일들
        List<MultipartFile> multipartList = postFileVO.getFiles();

        // 새롭게 전달되어온 파일들의 목록을 저장할 list 선언
        List<MultipartFile> addFileList = new ArrayList<>();

        if (CollectionUtils.isEmpty(dbImageList)) { // DB에 아예 존재 x
            if (!CollectionUtils.isEmpty(multipartList)) { // 전달되어온 파일이 하나라도 존재
                // 저장할 파일 목록에 추가
                addFileList.addAll(multipartList);
            }
        } else {  // DB에 한 장 이상 존재
            if (CollectionUtils.isEmpty(multipartList)) { // 전달되어온 파일 아예 x
                // 파일 삭제
                for (Image dbImage : dbImageList)
                    imageRepository.deleteImageByIid(dbImage.getIid(), pid);
            } else {  // 전달되어온 파일 한 장 이상 존재

                // DB에 저장되어있는 파일 원본명 목록
                List<String> dbOriginNameList = new ArrayList<>();

                // multipartFile형식은 String값으로 찾을 수 없으니 원본명만 빼서 리스트로 만듦
                List<String> multiList = multipartList.stream()
                        .map(MultipartFile::getOriginalFilename).collect(Collectors.toList());
                // DB의 파일 원본명 추출
                for (Image dbImage : dbImageList) {
                    // file id로 DB에 저장된 파일 정보 얻어오기
                    ImageDTO dbImageDTO = imageService.findByFileIid(dbImage.getIid());
                    // DB의 파일 원본명 얻어오기
                    String dbOriginFileName = dbImageDTO.getOriginFilename();

                    if (!multiList.contains(dbOriginFileName))  { // 서버에 저장된 파일들 중 전달되어온 파일이 존재하지 않는다면
                        log.info("파일 삭제 중 : " + dbImage.getOriginFileName());
                        imageRepository.deleteImageByIid(dbImage.getIid(), pid);
                    }
                    else  { // 그것도 아니라면
                        log.info("파일 추가 중 : " + dbOriginFileName);
                        dbOriginNameList.add(dbOriginFileName);    // DB에 저장할 파일 목록에 추가
                    }

                }

                for (MultipartFile multipartFile : multipartList) { // 전달되어온 파일 하나씩 검사
                    // 파일의 원본명 얻어오기
                    String multipartOriginName = multipartFile.getOriginalFilename();
                    if (!dbOriginNameList.contains(multipartOriginName)) {   // DB에 없는 파일이면
                        addFileList.add(multipartFile); // DB에 저장할 파일 목록에 추가
                    }
                }
            }
        }
            return responseService.getSingleResult(postService.update(pid, postRequestDTO, addFileList, request));
    }


    // 개별 조회
    @ApiOperation(value = "단일 검색", notes = "게시글 하나를 검색합니다.")
    @GetMapping(value = {"/post/{pid}", "/post/category/{pid}"})
    public ListResult<PostResponseDTO> searchById(
            @ApiParam(value = "게시글 번호", required = true)
            @PathVariable("pid") Long pid) {
        // 게시글 id로 해당 게시글 첨부파일 전체 조회
        List<ImageResponseDTO> imageResponseDTOList =
                imageService.findAllByPost(pid);
        // 게시글 첨부파일 id 담을 List 객체 생성
        List<Long> imageId = new ArrayList<>();
        // 각 첨부파일 id 추가
        for(ImageResponseDTO imageResponseDTO : imageResponseDTOList)
            imageId.add(imageResponseDTO.getFileId());
        List<PostResponseDTO> postList = new ArrayList<>();
        postList.add(postService.searchById(pid, imageId));
        // 게시글 id와 첨부파일 id 목록 전달받아 결과 반환
        return responseService.getListResult(postList);
    }




    // 게시글 삭제
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제합니다.")
    @DeleteMapping("/post/delete/{pid}")
    public SingleResult<Long> delete(
            @ApiParam(value = "게시글 번호", required = true)
            @PathVariable("pid") Long pid,
            HttpServletRequest request) {
        return responseService.getSingleResult(postService.delete(pid, request));
    }

    @ApiOperation(value = "카테고리 조회", notes = "카테고리로 게시글을 검색합니다.")
    @GetMapping("/post/category")
    public ListResult<PostListResponseDTO> searchCategory(
            @PageableDefault(sort = "pid", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category
    ) {
        // 반환할 List<BoardListResponseDto> 생성
        List<PostListResponseDTO> responseDTOList = new ArrayList<>();
        // 게시글 전체 조회
        Page<Post> list;

        log.info("=======================================");
        log.info("CATEGORY : " + category);
        log.info("KEYWORD : " + keyword);
        log.info("=======================================");
        if (keyword == null) {
            list = postService.searchByCategory(pageable, Category.valueOf(category));
        } else {
            list = postService.searchByKeywordWithCategory(pageable, keyword, Category.valueOf(category));
        }

        for(Post post : list){
            // 전체 조회하여 획득한 각 게시글 객체를 이용하여 BoardListResponseDto 생성
            PostListResponseDTO responseDto = new PostListResponseDTO(post);
            responseDTOList.add(responseDto);
        }

        return responseService.getListResult(responseDTOList);
    }

    // fild_Id가 썸네일 용 하나만 나옴 여러개 나올 수 있도록 변경 필요
    // 전체 조회
    @ApiOperation(value = "전체 조회", notes = "게시글 전체를 검색합니다.")
    @GetMapping("/post")
    public ListResult<PostListResponseDTO> searchAllDesc(
            @PageableDefault(sort = "pid", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword) {

        // 반환할 List<BoardListResponseDto> 생성
        List<PostListResponseDTO> responseDTOList = new ArrayList<>();
        // 게시글 전체 조회
        Page<Post> list;

        // 검색할 때와 검색하지 않았을 때를 구분
        if(keyword == null) {
            list = postService.searchAllDesc(pageable);
        } else {
            list = postService.searchByKeyword(pageable, keyword);
        }

        for(Post post : list){
            // 전체 조회하여 획득한 각 게시글 객체를 이용하여 BoardListResponseDto 생성
            PostListResponseDTO responseDto = new PostListResponseDTO(post);
            responseDTOList.add(responseDto);
        }

        return responseService.getListResult(responseDTOList);
    }

}
