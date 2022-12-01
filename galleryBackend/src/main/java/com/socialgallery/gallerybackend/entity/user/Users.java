package com.socialgallery.gallerybackend.entity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialgallery.gallerybackend.entity.BaseEntity;
import com.socialgallery.gallerybackend.entity.comment.Comment;
import com.socialgallery.gallerybackend.entity.post.Post;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Builder
@AllArgsConstructor // 모든 필드값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor  // parameter가 없는 기본 생성자를 생성
@Getter
@ToString(exclude = "post")
@Table(name = "users")
public class Users extends BaseEntity implements UserDetails, OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)         // GenerationType : JPA에서 기본키의 생성 전략 타입
    private Long id;                                        // GenerationType.AUTO : GenerationType을 자동으로 설정

    @Size(min = 4, max = 255, message = "Minimum username length: 4")
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // read 불가능 Json 결과로 출력 x
    @Column(nullable = false, length = 100)
    private String password;

    private String picture; // 구글이나 네이버 로그인 api 기능 사용시 필요

    private String phone;

    private boolean fromSocial; // 직접 회원가입 했는지, 구글이나 네이버 등으로 회원가입 했는지

    private boolean isLogin;

    @Builder.Default
    @OneToMany(mappedBy = "users", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Post> post = new ArrayList<>();

    @Builder.Default
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "users", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String providerId;

    /*
     * User 객체의 권한이 담긴 컬렉션 객체를 User 조회시 EAGER로 즉시로딩하지 않는다면,
     * Porxy객체가 담겨서 반환되므로 제대로 "ROLE_USER"를 확인할 수 없다.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {return this.password;}

    public void updateUsername(String username) {
        this.username = username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return false;
    }

    public Users update(String username, String picture) {
        this.username = username;
        this.picture = picture;

        return this;
    }

    public Users updateIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
        return this;
    }

    @Override
    public String getName() {
        return null;
    }
}
