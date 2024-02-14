package com.example.activity_service.client;

import com.example.activity_service.dto.request.ValidateRefreshTokenReq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user")
public interface UserServiceClient {

    @PostMapping("/users/internal/token")
    void validateRefreshToken(@RequestBody ValidateRefreshTokenReq validateRefreshTokenReq);

    @GetMapping("/users/internal/{userId}")
    void validateUserId(@PathVariable String userId);

    // Error 상황 연출을 위한 api
    @GetMapping("/users/internal/errorful/case1")
    ResponseEntity<String> case1();

    @GetMapping("/users/internal/errorful/case2")
    ResponseEntity<String> case2();

    @GetMapping("/users/internal/errorful/case3")
    ResponseEntity<String> case3();

}
