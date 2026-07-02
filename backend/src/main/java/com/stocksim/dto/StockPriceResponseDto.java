package com.stocksim.dto;

import com.stocksim.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

public record StockPriceResponseDto(
    Long id,
    String code,
    String name,
    BigDecimal currentPrice
) {
    public static StockPriceResponseDto from(Stock stock) {
        return new StockPriceResponseDto(
                stock.getId(),
                stock.getCode(),
                stock.getName(),
                stock.getCurrentPrice()
        );
    }
}
