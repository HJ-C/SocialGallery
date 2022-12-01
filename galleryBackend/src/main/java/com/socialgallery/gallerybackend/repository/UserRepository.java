package com.socialgallery.gallerybackend.repository;

import com.socialgallery.gallerybackend.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
//    @EntityGraph(attributePaths = {"roleSet"})
//    User findByEmail(String email);

    Optional<Users> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);
    // Optional<Users> findByUsername(String username);
}
