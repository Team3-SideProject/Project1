package com.stocksim.service;

import com.stocksim.dto.PortfolioResponse;
import com.stocksim.dto.PortfolioStockResponse;
import com.stocksim.entity.Portfolio;
import com.stocksim.entity.Stock;
import com.stocksim.entity.User;
import com.stocksim.repository.PortfolioRepository;
import com.stocksim.repository.StockRepository;
import com.stocksim.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PortfolioServiceTest {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Test
    @DisplayName("유저의 이메일로 포트폴리오를 조회하면 총자산, 총매수금액, 총손익 및 보유 주식 목록이 정확히 계산된다")
    void 포트폴리오_조회_테스트() {
        // given: 테스트를 위한 가짜 주식, 유저, 매수 데이터 세팅

        // 1. 주식 생성 (삼성전자 현재가: 70,000원 / 카카오 현재가: 50,000원)
        Stock samsung = stockRepository.save(new Stock(null, "005930", "삼성전자", "국전주", new BigDecimal("70000"), null, null));
        Stock kakao = stockRepository.save(new Stock(null, "035720", "카카오", "플랫폼", new BigDecimal("50000"), null, null));

        // 2. 유저 생성 (보유 현금: 500,000원)
        String targetEmail = "sangmin@test.com";
        User user = new User(targetEmail, "password", "안상민");
        user.increaseCash(new BigDecimal("500000"));
        userRepository.save(user);

        // 3. 포트폴리오(매수 기록) 생성
        // - 삼성전자: 10주 보유, 평균 매수가 65,000원
        //   -> 매수 금액 = 65,000 * 10 = 650,000원
        //   -> 평가 금액 = 70,000 * 10 = 700,000원
        portfolioRepository.save(new Portfolio(user, samsung, 10, new BigDecimal("65000")));

        // - 카카오: 5주 보유, 평균 매수가 55,000원
        //   -> 매수 금액 = 55,000 * 5 = 275,000원
        //   -> 평가 금액 = 50,000 * 5 = 250,000원
        portfolioRepository.save(new Portfolio(user, kakao, 5, new BigDecimal("55000")));

        System.out.println("===== 저장된 User =====");
        System.out.println(user);

        System.out.println("===== 저장된 Stocks =====");
        stockRepository.findAll().forEach(System.out::println);

        System.out.println("===== 저장된 Portfolio =====");
        portfolioRepository.findAll().forEach(System.out::println);

        portfolioRepository.findAll().forEach(p -> {
            System.out.println("user = " + p.getUser().getNickname());
            System.out.println("stock = " + p.getStock().getName());
            System.out.println("quantity = " + p.getQuantity());
            System.out.println("averageBuyPrice = " + p.getAverageBuyPrice());
            System.out.println("---------------------------");
        });

        /* * 💡 예상 결과값 암산
         * - 총 매수금액 (totalPurchase) = 650,000 + 275,000 = 925,000원
         * - 총 주식 평가액 = 700,000 + 250,000 = 950,000원
         * - 총 자산 (totalAsset) = 주식 평가액(950,000) + 보유 현금(500,000) = 1,450,000원
         * - 총 손익 (totalProfit) = 총 자산(1,450,000) - 총 매수금액(925,000) = 525,000원 (혹은 순수익 계산 방식에 따라 변동 가능)
         */

        // when: 포트폴리오 서비스 로직 실행
        PortfolioResponse response = portfolioService.getPortfolio(targetEmail);

        // ===== 결과 출력 =====
        System.out.println("========== Portfolio Response ==========");
        System.out.println("닉네임 : " + response.nickname());
        System.out.println("총 자산 : " + response.totalAsset());
        System.out.println("총 매수금액 : " + response.totalPurchase());
        System.out.println("총 손익 : " + response.totalProfit());
        System.out.println("보유 주식");

        response.stocks().forEach(stock -> {
            System.out.println("----------------------------");
            System.out.println("종목코드 : " + stock.code());
            System.out.println("종목명 : " + stock.name());
            System.out.println("보유수량 : " + stock.quantity());
            System.out.println("평균매수가 : " + stock.averageBuyPrice());
            System.out.println("현재가 : " + stock.currentPrice());
            System.out.println("평가금액 : " + stock.evaluationAmount());
        });

        System.out.println("========================================");

        // then: 연산 결과 검증
        assertThat(response).isNotNull();
        assertThat(response.nickname()).isEqualTo("안상민");

        // 1. 자산 금액 검증
        assertThat(response.totalAsset()).isEqualByComparingTo(new BigDecimal("1450000"));
        assertThat(response.totalPurchase()).isEqualByComparingTo(new BigDecimal("925000"));
        // ⚠️ 주의: 만약 총손익 계산을 (총자산 - 총매수금액)이 아니라 (주식평가액 - 총매수금액)으로 구현하셨다면 이 숫자를 '25000' 등으로 조정하세요!
        assertThat(response.totalProfit()).isEqualByComparingTo(new BigDecimal("525000"));

        // 2. 주식 목록 검증 (삼성, 카카오 2종목)
        List<PortfolioStockResponse> stocks = response.stocks();
        assertThat(stocks).hasSize(2);

        // 삼성전자 데이터가 올바르게 들어갔는지 검증
        PortfolioStockResponse samsungResult = stocks.stream()
                .filter(s -> s.code().equals("005930"))
                .findFirst()
                .orElseThrow();
        assertThat(samsungResult.name()).isEqualTo("삼성전자");
        assertThat(samsungResult.quantity()).isEqualTo(10);
        assertThat(samsungResult.averageBuyPrice()).isEqualByComparingTo(new BigDecimal("65000"));
        assertThat(samsungResult.currentPrice()).isEqualByComparingTo(new BigDecimal("70000"));
        assertThat(samsungResult.evaluationAmount()).isEqualByComparingTo(new BigDecimal("700000"));
    }
}