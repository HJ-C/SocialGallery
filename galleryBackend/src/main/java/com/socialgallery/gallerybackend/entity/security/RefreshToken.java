package com.socialgallery.gallerybackend.entity.security;

import com.socialgallery.gallerybackend.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 * token이 DB에 저장되는 index로서 Id를 지정해주고 객체를 가져오기 위한 용도로서 key값은 따로 만들어준다.
 * 토큰의 claims의 값은 공개값이므로 유저를 특정할 수 없는 userPk값을 key값으로 해준다.
 * BaseEntity는 createdDate, modifedDate가 존재하므로 상속받아서 추후 expire 시간과 비교해서 만료시켜준다.
 */

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "key_")
    private Long key;

    @Column(nullable = false)
    private String token;

    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }

    @Builder
    public RefreshToken(Long key, String token) {
        this.key = key;
        this.token = token;
    }
}
