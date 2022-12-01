package com.socialgallery.gallerybackend.service.image;

import com.socialgallery.gallerybackend.advice.exception.ImageNotFoundCException;
import com.socialgallery.gallerybackend.dto.image.ImageDTO;
import com.socialgallery.gallerybackend.dto.image.ImageResponseDTO;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    /**
     * 이미지 개별 조회
     */
    @Transactional(readOnly = true)
    public ImageDTO findByFileIid(Long iid) {
        Image image = imageRepository.findById(iid).orElseThrow(ImageNotFoundCException::new);

        ImageDTO imageDTO = ImageDTO.builder()
                .originFilename(image.getOriginFileName())
                .filePath(image.getFilePath())
                .fileSize(image.getFileSize())
                .build();

        return imageDTO;
    }

    /**
     * 이미지 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ImageResponseDTO> findAllByPost(Long pid) {
        List<Image> imageList = imageRepository.findAllByPostPid(pid);

        return imageList.stream()
                .map(ImageResponseDTO::new)
                .collect(Collectors.toList());
    }

}
