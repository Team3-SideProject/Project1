package com.stocksim.service;

import com.stocksim.entity.Portfolio;
import com.stocksim.entity.Ranking;
import com.stocksim.entity.Stock;
import com.stocksim.entity.User;
import com.stocksim.repository.PortfolioRepository;
import com.stocksim.repository.RankingRepository;
import com.stocksim.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingSchedulerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RankingScheduler rankingScheduler;

    @Test
    void 랭킹_계산_테스트() {

        // ==========================
        // User 생성
        // ==========================
        User user1 = new User("a@test.com", "1234", "철수");
        User user2 = new User("b@test.com", "1234", "영희");

        setField(user1, "id", 1L);
        setField(user2, "id", 2L);

        setField(user1, "cash", new BigDecimal("1000000"));
        setField(user2, "cash", new BigDecimal("500000"));

        // ==========================
        // Stock 생성
        // ==========================
        Stock samsung = new Stock("005930", "삼성전자", new BigDecimal("70000"));
        Stock naver = new Stock("035420", "NAVER", new BigDecimal("200000"));

        // ==========================
        // Portfolio 생성
        // ==========================
        Portfolio p1 = new Portfolio(user1, samsung, 10, new BigDecimal("60000"));
        Portfolio p2 = new Portfolio(user1, naver, 2, new BigDecimal("180000"));
        Portfolio p3 = new Portfolio(user2, samsung, 3, new BigDecimal("60000"));

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        when(portfolioRepository.findAllWithUserAndStock())
                .thenReturn(List.of(p1, p2, p3));

        // ==========================
        // 실행
        // ==========================
        rankingScheduler.calculateTotalAssetRanking();

        // ==========================
        // 저장 결과 가져오기
        // ==========================
        ArgumentCaptor<List<Ranking>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(rankingRepository).saveAll(captor.capture());

        List<Ranking> result = captor.getValue();

        // ==========================
        // 로그 출력
        // ==========================
        System.out.println();
        System.out.println("============== Ranking Result ==============");

        for (Ranking r : result) {

            System.out.println("-------------------------------------------");
            System.out.println("순위      : " + r.getRanking());
            System.out.println("닉네임    : " + r.getNickname());
            System.out.println("유저ID    : " + r.getUserId());
            System.out.println("총자산    : " + r.getTotalAsset());
            System.out.println("집계시간  : " + r.getCalculatedAt());
        }

        System.out.println("-------------------------------------------");
        System.out.println("===========================================");
        System.out.println();

        // ==========================
        // 결과 검증
        // ==========================

        assertEquals(2, result.size());

        // 1등
        assertEquals("철수", result.get(0).getNickname());
        assertEquals(1, result.get(0).getRanking());
        assertEquals(new BigDecimal("2100000"), result.get(0).getTotalAsset());

        // 2등
        assertEquals("영희", result.get(1).getNickname());
        assertEquals(2, result.get(1).getRanking());
        assertEquals(new BigDecimal("710000"), result.get(1).getTotalAsset());

        // calculatedAt 저장 여부
        assertNotNull(result.get(0).getCalculatedAt());
        assertNotNull(result.get(1).getCalculatedAt());

        // Repository 호출 순서 검증
        InOrder inOrder = inOrder(rankingRepository);

        inOrder.verify(rankingRepository).deleteAllInBatch();
        inOrder.verify(rankingRepository).saveAll(any());

        verifyNoMoreInteractions(rankingRepository);
    }

    /**
     * private 필드 세팅
     */
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}