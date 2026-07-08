package com.stocksim.service;

import com.stocksim.dto.RankingResponse;
import com.stocksim.entity.Ranking;
import com.stocksim.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    @Transactional(readOnly = true)
    public List<RankingResponse> getRankings() {

        List<Ranking> rankings =
                rankingRepository.findAllByOrderByRankingAsc();

        return rankings.stream()
                .limit(100) // 100등까지 조회
                .map(ranking -> new RankingResponse(
                        ranking.getUserId(),
                        ranking.getNickname(),
                        ranking.getTotalAsset(),
                        ranking.getRanking()
                ))
                .toList();
    }
}