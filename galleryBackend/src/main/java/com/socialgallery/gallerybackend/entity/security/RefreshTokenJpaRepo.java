package com.socialgallery.gallerybackend.entity.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036
 */

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, String > {

    Optional<RefreshToken> findByKey(Long key);
}
