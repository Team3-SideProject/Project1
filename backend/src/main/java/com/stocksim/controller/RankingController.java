package com.stocksim.controller;

import com.stocksim.config.JwtTokenProvider;
import com.stocksim.dto.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList; //추가
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/rankings/assets")
    public List<RankingResponse> getRankings(@RequestHeader("Authorization") String authorization) {

        String token = authorization.substring(7); // "Bearer " 제거

        String email = jwtTokenProvider.getEmail(token);

        return getRankings(email);
    }
}
