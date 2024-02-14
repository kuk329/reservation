package com.example.activity_service.controller;

import com.example.activity_service.client.UserServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/activity/errorful")
@RestController
public class ErrorController {

    private final UserServiceClient userServiceClient;


    @CircuitBreaker(name = "case1" ,fallbackMethod = "fallBackMethod")
    @Retry(name="retryCase" ,fallbackMethod = "retryFallBackMethod")
    @GetMapping("/case1")
    public ResponseEntity<String> case1() {
        System.out.println("case1 함수 진입");
        ResponseEntity<String> result = userServiceClient.case1();
        return result;
    }

    @CircuitBreaker(name = "case1" ,fallbackMethod = "fallBackMethod")
    @Retry(name="retryCase",fallbackMethod = "retryFallBackMethod" )
    @GetMapping("/case2")
    public ResponseEntity<String> case2() {
        System.out.println("case2 함수 진입");
        ResponseEntity<String> result = userServiceClient.case2();
        return result;
    }

    @CircuitBreaker(name = "case1" ,fallbackMethod = "fallBackMethod")
    @Retry(name="retryCase",fallbackMethod = "retryFallBackMethod" )
    @GetMapping("/case3")
    public ResponseEntity<String> case3() {
        System.out.println("case3 함수 진입");
        ResponseEntity<String> result = userServiceClient.case3();
        return result;
    }

    private String retryFallBackMethod(Throwable throwable){
        log.error("재시도 횟수 종료 ");
        return throwable.getMessage();
    }

    private String fallBackMethod(Throwable throwable){
        log.error("특정 횟수 지연 또는 무응답으로 circuit open");
        return throwable.getMessage();
    }

}
