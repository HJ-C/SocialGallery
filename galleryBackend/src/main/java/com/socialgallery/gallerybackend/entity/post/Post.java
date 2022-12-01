package com.socialgallery.gallerybackend.entity.post;

import com.socialgallery.gallerybackend.entity.BaseEntity;
import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.image.Image;
import com.socialgallery.gallerybackend.entity.user.Users;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/*
 * @Reference https://velog.io/@yu-jin-song/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%EA%B5%AC%ED%98%84-5-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%88%98%EC%A0%95-%EB%B0%8F-%EC%82%AD%EC%A0%9C-%EB%8B%A4%EC%A4%91-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%B0%98%ED%99%98-%EB%B0%8F-%EC%A1%B0%ED%9A%8C-%EC%B2%98%EB%A6%AC-MultipartFile
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@ToString
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false)
    private String title;

    private String content;

    private int hits;

    private int reviewCnt;

    private int likeCnt;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Users.class)
    private Users users;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    // LazyInitializationException: could not initialize proxy - no Session Error 발생으로 @LazyCollection옵션 넣어줌
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "post",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "post",
    cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(Long pid, Users users, String title, String content, int hits, int reviewCnt, int likeCnt,
                String category) {
        this.pid = pid;
        this.users = users;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.reviewCnt = reviewCnt;
        this.category = Category.valueOf(category);
        this.likeCnt = likeCnt;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // post에서 파일 처리 위함
    public void addImage(Image image) {
        this.images.add(image);

        // 게시글에 파일이 저장되어 있지 않은 경우
        if (image.getPost() != this)
            // 파일 저장
            image.setPost(this);
    }
}
