package com.stocksim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.stocksim.entity.Ranking;
import com.stocksim.dto.RankingResponse;
import com.stocksim.repository.RankingRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.stocksim.service.RankingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RankingService rankingService;

    @Test
    @DisplayName("랭킹 조회 - 100명 이하일 때 전체 조회 성공")
    void 랭킹_조회_100명_이하_성공() {
        // given
        List<Ranking> mockRankings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Ranking 엔티티 생성자: (int ranking, Long userId, String nickname, BigDecimal totalAsset, LocalDateTime calculatedAt)
        mockRankings.add(new Ranking(1, 10L, "유저1", new BigDecimal("50000"), now));
        mockRankings.add(new Ranking(2, 20L, "유저2", new BigDecimal("40000"), now));
        mockRankings.add(new Ranking(3, 30L, "유저3", new BigDecimal("30000"), now));

        given(rankingRepository.findAllByOrderByRankingAsc()).willReturn(mockRankings);

        // when
        System.out.println("====== [테스트 시작] 랭킹 조회 (데이터 3개) ======");
        List<RankingResponse> result = rankingService.getRankings();

        // then
        System.out.println("[반환된 랭킹 리스트 크기]: " + result.size());

        // 🌟 Record 핵심: getNickname()이 아니라 nickname()으로 호출해야 합니다.
        result.forEach(response ->
                System.out.println(String.format("순위: %d등 | 유저ID: %d | 닉네임: %s | 자산: %s원",
                        response.ranking(), response.userId(), response.nickname(), response.totalAsset()))
        );
        System.out.println("=================================================");

        assertThat(result).hasSize(3);
        assertThat(result.get(0).nickname()).isEqualTo("유저1");
        assertThat(result.get(0).ranking()).isEqualTo(1);
        assertThat(result.get(0).totalAsset()).isEqualByComparingTo(new BigDecimal("50000"));

        verify(rankingRepository).findAllByOrderByRankingAsc();
    }

    @Test
    @DisplayName("랭킹 조회 - 100명이 넘을 때 최대 100명까지만 제한하여 반환")
    void 랭킹_조회_100명_제한_성공() {
        // given
        List<Ranking> mockRankings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 1; i <= 120; i++) {
            mockRankings.add(new Ranking(
                    i,
                    (long) i,
                    "유저" + i,
                    new BigDecimal(10000 * i),
                    now
            ));
        }

        given(rankingRepository.findAllByOrderByRankingAsc()).willReturn(mockRankings);

        // when
        System.out.println("====== [테스트 시작] 랭킹 조회 (데이터 120개) ======");
        List<RankingResponse> result = rankingService.getRankings();

        // then
        System.out.println("[반환된 랭킹 리스트 크기]: " + result.size() + " (원본 데이터: 120개)");
        System.out.println("[마지막 순위 확인]: " + result.get(result.size() - 1).ranking() + "등");
        System.out.println("==================================================");

        assertThat(result).hasSize(100);
        assertThat(result.get(99).ranking()).isEqualTo(100);
        assertThat(result.get(99).nickname()).isEqualTo("유저100");

        verify(rankingRepository).findAllByOrderByRankingAsc();
    }
}