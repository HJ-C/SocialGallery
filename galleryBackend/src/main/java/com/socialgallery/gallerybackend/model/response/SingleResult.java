package com.socialgallery.gallerybackend.model.response;

import lombok.Getter;
import lombok.Setter;

/*
 * @Reference https://ws-pace.tistory.com/70?category=964036
 *
 * 단일응답모델
 *
 * <T> = Generic Type, 다른 엔티티에도 적용시키기 위함
 */

@Getter
@Setter
public class SingleResult<T> extends CommonResult {
    private T data;

}
