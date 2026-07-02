package com.stocksim.controller;

import com.stocksim.dto.RankingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList; //추가
import java.util.List;

@RestController
public class RankingController {

    @GetMapping("/api/rankings/assets")
    public List<RankingResponse> getRankings() { // TODO: 리턴 타입 변경 + 랭킹 조회 로직 구현
        return new ArrayList<>();
    }
}
