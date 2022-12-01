package com.socialgallery.gallerybackend.config;

import com.socialgallery.gallerybackend.config.security.JwtProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //모든 경로에 대해
        registry.addMapping("/**")
            // Origin이 http:localhost:3000에 대해
            .allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000")
            // GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(true)
            .maxAge(MAX_AGE_SECS);

    }


}
