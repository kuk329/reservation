package com.example.activity_service.common.jwt;


import static com.example.activity_service.common.response.BaseResponseStatus.TOKEN_INVALID;

import com.example.activity_service.client.UserServiceClient;
import com.example.activity_service.common.exceptions.BaseException;
import com.example.activity_service.dto.request.ValidateRefreshTokenReq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter { // JWT 검증 필터 -> 헤더로 들어온 jwt 토큰을 검증

    private static final String[] whiteList = {"/activity/internal/**"};
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String COOKIE_NAME = "refreshToken";
    private final JWTUtil jwtUtil;

    private final UserServiceClient userServiceClient;

    public JWTFilter(JWTUtil jwtUtil, UserServiceClient userServiceClient){
        this.jwtUtil = jwtUtil;
        this.userServiceClient = userServiceClient;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("================== JWTFilter 실행 ==================");

        String requestURI = request.getRequestURI(); // 요청한 경로
        log.info("요청 경로 {}",requestURI);
        // 로그인 권한이 필요 없는 경로 처리
        if(!isLoginCheckPath(requestURI)){
            log.info("로그인 인증이 필요없는 경로이므로 jwtFilter 통과");
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("authorization header 값 : {}",authorization);

        String refreshToken = "";

        // 헤더에 들어있는 쿠키 정보 출력
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("cookie name : {}",cookie.getName());
                log.info("cookie value : {}",cookie.getValue());
                if (Objects.equals(cookie.getName(), COOKIE_NAME)) {
                    refreshToken = cookie.getValue();
                    log.info("refreshToken 확인 : {}",refreshToken);
                }
            }
        }


        // important !

        if(authorization!=null){
            log.info("헤더에 값 존재");
            if(authorization.startsWith("Bearer ")) {
                log.info("헤더에 Bearer token 존재");
                String accessToken = authorization.split(" ")[1];
                if(!jwtUtil.isExpired(accessToken)){
                    log.info("accessToken 값 만료안됨.");
                    String userId = jwtUtil.getUserId(accessToken);
                    String userRole = jwtUtil.getRole(accessToken);
                    authenticateUser(userId,userRole);
                    log.info("accessToken 인증 성공");



                }else{ // refreshToken 확인
                    log.info("accessToken is Expired");
                    log.info("refreshToken 확인 시작");
                    String[] strings = validateRefreshToken(refreshToken);
                    issuedNewAccessToken(response,strings[0],strings[1]);
                    authenticateUser(strings[0],strings[1]);
                    log.info("refreshToken 확인 성공");
                }
            }else{
                log.info("헤더에 Bearer token 없음");
                log.info("refreshToken 확인 시작");
                String[] strings = validateRefreshToken(refreshToken);
                issuedNewAccessToken(response,strings[0],strings[1]);
                authenticateUser(strings[0],strings[1]);
                log.info("refreshToken 확인 성공");
            }
        }else{
            log.info("authorization is null");
            log.info("refreshToken 확인 시작");
            String[] strings = validateRefreshToken(refreshToken);
            issuedNewAccessToken(response,strings[0],strings[1]);
            authenticateUser(strings[0],strings[1]);
            log.info("refreshToken 확인 성공");
        }

        System.out.println("================== JWTFilter 종료 ==================");

        filterChain.doFilter(request,response);


    }

    private String[] validateRefreshToken(String refreshToken) {

        String[] result = new String[2];

        if (Objects.equals(refreshToken, "")) {
            throw new BaseException(TOKEN_INVALID);
        }
        if (jwtUtil.isExpired(refreshToken)) {
            throw new BaseException(TOKEN_INVALID);
        }
        String userId = jwtUtil.getUserId(refreshToken);
        userServiceClient.validateRefreshToken(new ValidateRefreshTokenReq(refreshToken,userId));
        String userRole = jwtUtil.getRole(refreshToken);
        result[0] = userId;
        result[1] = userRole;
        log.info("refreshToken 검증 완료");
        return result;
    }

    private void issuedNewAccessToken(HttpServletResponse response, String userId, String role) {
        log.info("새로운 accessToken 발급");
        String newAccess = jwtUtil.createToken(userId, role, "ACCESS");
        response.addHeader("Authorization", "Bearer " + newAccess);

    }

    private void authenticateUser(String userId, String role) {

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(userId,null,null);
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

     //  userServiceClient.validateUserId(userId);


    }

    private boolean isLoginCheckPath(String requestURI){
        return !PatternMatchUtils.simpleMatch(whiteList,requestURI);
    }
}
