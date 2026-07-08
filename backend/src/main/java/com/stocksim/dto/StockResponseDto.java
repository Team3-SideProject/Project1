package com.stocksim.dto;

import com.stocksim.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

public record StockResponseDto(
    Long id,
    String code,
    String name,
    BigDecimal currentPrice
) {
    public static StockResponseDto from(Stock stock) {
        return new StockResponseDto(
                stock.getId(),
                stock.getCode(),
                stock.getName(),
                stock.getCurrentPrice()
        );
    }
}