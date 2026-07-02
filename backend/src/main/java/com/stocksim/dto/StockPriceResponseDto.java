package com.stocksim.stock.dto;

import com.stocksim.stock.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceResponseDto {
    private Long id;
    private String code;
    private String name;
    private BigDecimal currentPrice;

    public static StockPriceResponseDto from(Stock stock) {
        return new StockPriceResponseDto(
                stock.getId(),
                stock.getCode(),
                stock.getName(),
                stock.getCurrentPrice()
        );
    }
}
