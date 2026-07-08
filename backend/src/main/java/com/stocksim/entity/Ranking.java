package com.stocksim.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rankings") // 추후 테이블 추가 예정
@Getter
@NoArgsConstructor
public class Ranking { // 스케쥴러로 일정 시간에 랭킹 집계 후 db에 반영

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ranking; // 등수

    private Long userId;
    private String nickname;
    private BigDecimal totalAsset;

    private LocalDateTime calculatedAt; // 집계 시간

    public Ranking(int ranking, Long userId, String nickname, BigDecimal totalAsset, LocalDateTime calculatedAt) {
        this.ranking = ranking;
        this.userId = userId;
        this.nickname = nickname;
        this.totalAsset = totalAsset;
        this.calculatedAt = calculatedAt;
    }
}