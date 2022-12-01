package com.socialgallery.gallerybackend.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * @Reference https://ws-pace.tistory.com/70?category=964036
 */

@Getter
@AllArgsConstructor
public enum CommonResponse {
    SUCCESS(0, "성공하였습니다."),
    FAIL(-1, "실패하였습니다.");

    private int code;
    private String msg;
}
