package com.stocksim.service;

import com.stocksim.dto.RankingResponse;
import com.stocksim.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {
    private final PortfolioRepository portfolioRepository;
    public RankingService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public List<RankingResponse> getRankings() {
        // TODO: 랭킹 조회 로직 구현
        // 포트폴리오 전재산 순으로 나열 -> 그 순서대로 userid, nickname, 전재산을 RankingResponse
    }

    public void calcRanking(){
        // TODO: 랭킹 계산 로직 구현
    }
}
