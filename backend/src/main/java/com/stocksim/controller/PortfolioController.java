package com.stocksim.controller;

import com.stocksim.config.JwtTokenProvider;
import com.stocksim.dto.PortfolioResponse;
import com.stocksim.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "포트폴리오 조회", security = @SecurityRequirement(name = "JWT_AUTH"))
    @GetMapping("/me") // 포트폴리오 가져오기
    public PortfolioResponse getPortfolio(@RequestHeader("Authorization") String authorization) { // TODO : 리턴 타입 변경 + service 로직 변경
        String token = authorization.substring(7); // "Bearer " 제거

        String email = jwtTokenProvider.getEmail(token);

        return portfolioService.getPortfolio(email);
    }
}