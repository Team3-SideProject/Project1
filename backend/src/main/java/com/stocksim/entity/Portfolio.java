package com.stocksim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "portfolios")
@Getter
@NoArgsConstructor
public class Portfolio { // TODO : JPA 공부해서 채워 넣기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "stock_id")
    private long stockId;

    private int quantity;

    @Column(name = "average_buy_price")
    private BigDecimal averageBuyPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //생성자
    public Portfolio(
            Long userId,
            Long stockId,
            Integer quantity,
            BigDecimal averageBuyPrice
    ) {
        this.userId = userId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
    }

    // 매수 이후 수량 변경
    public void buy(Integer buyQuantity, BigDecimal buyPrice) {
        BigDecimal currentTotal = averageBuyPrice.multiply(BigDecimal.valueOf(quantity)); // 가지고있는 해당 총 주식금액
        BigDecimal buyTotal = buyPrice.multiply(BigDecimal.valueOf(buyQuantity)); // 이번에 구매하는 주식 총 금액

        int newQuantity = quantity + buyQuantity; // 구매후 보유 주식량

        //평균가 최신화
        this.averageBuyPrice =
                currentTotal.add(buyTotal)
                        .divide(BigDecimal.valueOf(newQuantity), 2, RoundingMode.HALF_UP);

        this.quantity = newQuantity;
    }
    public void sell(Integer sellQuantity) {
        if(quantity < sellQuantity) {
            throw new IllegalArgumentException("보유 수량이 부족합니다");
        }
        this.quantity -= sellQuantity;
    }
}

