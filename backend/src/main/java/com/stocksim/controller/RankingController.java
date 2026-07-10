package com.stocksim.controller;

import com.stocksim.dto.RankingResponse;
import com.stocksim.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public List<RankingResponse> getRankings() {
        return rankingService.getRankings();
    }
}