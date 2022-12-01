package com.socialgallery.gallerybackend.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // 공통 매핑 정보가 필요할 때 사용
@EntityListeners(AuditingEntityListener.class)              // @EntityListeners : Entity에 대한 메서드(insert, delete 등)
public abstract class BaseEntity {                          // 이 호출되기 전이나 후 등에 실행되는 메서드 @PrePersist, @PreUpdate
                                                            // 등이 있다.
    @CreatedDate                                            // AuditingEntityListeners.class : Spring JPA에서 기본으로
    @Column(name = "regDate", updatable = false)            // 제공하는 클래스. @CreatedDate, @LastModifiedDate 등이 있다.
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "modDate")
    private LocalDateTime modDate;
}
