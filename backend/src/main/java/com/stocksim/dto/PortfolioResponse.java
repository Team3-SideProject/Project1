package com.stocksim.dto;

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
) { // TODO : JPA 공부해서 채워 넣기

}
