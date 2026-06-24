package com.stocksim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 이 클래스는 api를 담당한다
public class HealthController {

    @GetMapping("/health") // /health 요청이 들어오면 아래를 실행한다
    public String health() {
        return "OK"; // 브라우저에게 응답한다
    }
}