package com.stocksim.dto;

import java.math.BigDecimal;

public record PortfolioStockResponse(
        String code,                  // 종목 코드
        String name,                  // 종목 이름
        int quantity,                 // 보유 수량
        BigDecimal averageBuyPrice,   // 평균 매수가
        BigDecimal currentPrice,      // 현재가
        BigDecimal evaluationAmount   // 평가금액 = 현재가 × 수량
) {
}