package com.stocksim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Getter
@NoArgsConstructor
public class Stock { // DB의 stocks 테이블과 연결되는 자바 객체입니다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB 정보를 stock 으로 가져오는 과정의 코드입니다.
    private Long id;

    private String code;

    private String name;

    private String description;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}