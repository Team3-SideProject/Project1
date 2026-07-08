package com.stocksim.dto;

import com.stocksim.entity.Portfolio;
import com.stocksim.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record PortfolioResponse(
        // 아이디
        // 닉네임
        // 총 자산 : 주식 + cash
        // 총 매수금액
        // 총 손익 : 총 자산 - 총 매수금액
        // List<PortfolioStockDto> stocks
        //                          ├── PortfolioStockDto (삼성)
        //                          ├── PortfolioStockDto (LG)
        //                          └── PortfolioStockDto (카카오)

        Long id,
        String nickname,
        BigDecimal totalAsset,
        BigDecimal totalPurchase,
        BigDecimal totalProfit,
        List<PortfolioStockResponse> stocks
) {
    // User 엔티티와 fetch join으로 긁어온 Portfolio 리스트를 받아 DTO로 변환하는 메서드
    public static PortfolioResponse of(User user, List<Portfolio> portfolios) {

        // 1. 각 주식별 응답 DTO 생성 및 총 매수금액, 총 평가금액 계산
        List<PortfolioStockResponse> stockResponses = portfolios.stream()
                .map(p -> {
                    BigDecimal currentPrice = p.getStock().getCurrentPrice();
                    BigDecimal quantity = BigDecimal.valueOf(p.getQuantity());
                    BigDecimal evaluationAmount = currentPrice.multiply(quantity); // 현재가 * 수량

                    return new PortfolioStockResponse(
                            p.getStock().getCode(),
                            p.getStock().getName(),
                            p.getQuantity(),
                            p.getAverageBuyPrice(),
                            currentPrice,
                            evaluationAmount
                    );
                })
                .collect(Collectors.toList());

        // 2. 총 매수금액 계산 (Σ(평균매수가 * 수량))
        BigDecimal totalPurchase = portfolios.stream()
                .map(p -> p.getAverageBuyPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. 총 주식 평가금액 계산 (Σ(현재가 * 수량))
        BigDecimal totalEvaluation = stockResponses.stream()
                .map(PortfolioStockResponse::evaluationAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 총 자산 = 보유 현금(cash) + 주식 평가금액 총합
        BigDecimal totalAsset = user.getCash().add(totalEvaluation);

        // 5. 총 손익 = 총 자산 - 총 매수금액 (혹은 총 평가금액 - 총 매수금액 기획에 따라 변경 가능)
        BigDecimal totalProfit = totalAsset.subtract(totalPurchase);

        return new PortfolioResponse(
                user.getId(),
                user.getNickname(),
                totalAsset,
                totalPurchase,
                totalProfit,
                stockResponses
        );
    }

}
