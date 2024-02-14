package com.example.activity_service.controller;

import com.example.activity_service.client.UserServiceClient;
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


    @Retry(name="ErrorCase1" )
    @GetMapping("/case1")
    public ResponseEntity<String> case1() {
        System.out.println("case1 함수 진입");
        ResponseEntity<String> result = userServiceClient.case1();
        return result;
    }

    @Retry(name="ErrorCase2" )
    @GetMapping("/case2")
    public ResponseEntity<String> case2() {
        System.out.println("case2 함수 진입");
        ResponseEntity<String> result = userServiceClient.case2();
        return result;
    }

    @Retry(name="ErrorCase3" )
    @GetMapping("/case3")
    public ResponseEntity<String> case3() {
        System.out.println("case3 함수 진입");
        ResponseEntity<String> result = userServiceClient.case3();
        return result;
    }

}
