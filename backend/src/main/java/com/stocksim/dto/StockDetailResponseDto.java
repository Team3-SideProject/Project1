package com.stocksim.dto;

import com.stocksim.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockDetailResponseDto(
    Long id,
    String code,
    String name,
    String description,
    BigDecimal currentPrice,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static StockDetailResponseDto from(Stock stock) {
        return new StockDetailResponseDto(
                stock.getId(),
                stock.getCode(),
                stock.getName(),
                stock.getDescription(),
                stock.getCurrentPrice(),
                stock.getCreatedAt(),
                stock.getUpdatedAt()
        );
    }
}
