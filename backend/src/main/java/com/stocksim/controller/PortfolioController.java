package com.stocksim.controller;

import com.stocksim.dto.PortfolioResponse;
import com.stocksim.service.PortfolioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/api/portfolio/me") // 포트폴리오 가져오기
    public List<PortfolioResponse> getPortfolio() { // TODO : 리턴 타입 변경 + service 로직 변경
        portfolioService.getPortfolio();
    }

}