package com.socialgallery.gallerybackend.model.response;

/*
 * @Reference https://ws-pace.tistory.com/70?category=964036
 *
 * 다중응답모델
 *
 * <T> = Generic Type, 다른 엔티티에도 적용시키기 위함
 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListResult<T> extends CommonResult {
    private List<T> list;
}
