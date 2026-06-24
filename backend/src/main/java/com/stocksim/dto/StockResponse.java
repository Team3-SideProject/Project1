package com.stocksim.dto;

import com.stocksim.entity.Stock;

import java.math.BigDecimal;

public record StockResponse( // 이곳은 DB 객체를 프론트로 보내는 응답용 객체로 바꾸는곳 입니다.
        Long id,
        String code,
        String name,
        String description,
        BigDecimal currentPrice
) {
    public static StockResponse from(Stock stock) {
        return new StockResponse(
                stock.getId(),
                stock.getCode(),
                stock.getName(),
                stock.getDescription(),
                stock.getCurrentPrice()
        );
    }
}