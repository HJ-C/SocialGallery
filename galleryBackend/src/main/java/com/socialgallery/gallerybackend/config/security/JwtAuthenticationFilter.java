package com.socialgallery.gallerybackend.config.security;

import com.socialgallery.gallerybackend.service.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * @Reference https://ws-pace.tistory.com/87?category=964036,
 * https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-2/
 * Request Header에서 토큰 쿼리값에 해당하는 value값을 통해 토큰을 가져와서 검증을 한다.
 * 검증이 완료되면  토큰에서 유저 정보를 추출해 Anthentication 객체로 반환하고, 해당 정보를 SecurityContextHolder에 저장한다.
 * 이로서 해당 유저는 Authenticate (인증)된다.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final CustomUserDetailService customUserDetailService;


//    public JwtAuthenticationFilter(JwtProvider jwtProvider,
//                                   CustomUserDetailService customUserDetailService) {
//        this.jwtProvider = jwtProvider;
//        this.customUserDetailService = customUserDetailService;
//    }

    //request로 들어오는 jwt의 유효성을 검증 - JwtProvider.vaildationToken()을 필터로서 FilterChain에 추가
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                         HttpServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        try {

            // request에서 token을 취한다.
            String token = jwtProvider.resolveToken((HttpServletRequest) request);

            // 검증
            log.info("[Verifying token]");
            log.info(((HttpServletRequest) request).getRequestURL().toString());

            if (StringUtils.hasText(token) && jwtProvider.validationToken(token)) {
                Long userId = Long.valueOf(jwtProvider.getUserId(token));

                UserDetails userDetails = customUserDetailService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

//        if (token != null && jwtProvider.validationToken(token)) {
//            Authentication authentication = jwtProvider.getAuthentication(token);
//            log.info("AUTHENTICATION : " + authentication);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
