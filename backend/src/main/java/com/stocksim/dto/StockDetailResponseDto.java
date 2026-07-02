package com.stocksim.stock.dto;

import com.stocksim.stock.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDetailResponseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal currentPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
