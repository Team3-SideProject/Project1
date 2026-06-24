package com.stocksim.dto;

import com.stocksim.entity.Trade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TradeResponse ( // 프론트로 보낼 데이터 목록
    Long id,
    Long userId,
    Long stockId,
    String tradeType,
    Integer quantity,
    BigDecimal price,
    BigDecimal totalAmount,
    LocalDateTime createdAt
) {
    public static TradeResponse from(Trade trade) { // Entity 하나를 Response 하나로 바꿈
        return new TradeResponse(
                trade.getId(),
                trade.getUserId(),
                trade.getStockId(),
                trade.getTradeType(),
                trade.getQuantity(),
                trade.getPrice(),
                trade.getTotalAmount(),
                trade.getCreatedAt()
        );
    }
}
