package com.stocksim.dto;

import java.math.BigDecimal;
import java.util.List;

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

}
