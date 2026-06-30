package com.stocksim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "portfolios")
@Getter
@NoArgsConstructor
public class Portfolio { // TODO : JPA 공부해서 채워 넣기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private long stockId;

    private int quantity;

    @Column(name = "average_buy_price")
    private BigDecimal averageBuyPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
