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
public class StockResponseDto {
    private Long id;
    private String code;
    private String name;
    private BigDecimal currentPrice;

    public static StockResponseDto from(Stock stock) {
        return new StockResponseDto(
                stock.getId(),
                stock.getCode(),
                stock.getName(),
                stock.getCurrentPrice()
        );
    }
}
