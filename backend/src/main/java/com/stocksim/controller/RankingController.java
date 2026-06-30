package com.stocksim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RankingController {

    @GetMapping("/api/rankings/assets")
    public void getRankings() { // TODO: 리턴 타입 변경 + 랭킹 조회 로직 구현

    }
}
