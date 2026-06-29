package com.stocksim.entity;
import jakarta.persistence.*; //JPA

import lombok.Getter; // getter 메서드 자동 생성
import lombok.NoArgsConstructor; // 기본 생성자 자동 생성

import java.math.BigDecimal;
import java.time.LocalDateTime;
// 이 코드는 DB 에서 자바로 데이터를 가져와 변환 해주는 코드입니다.
// trades 테이블을 자바 객체로 매핑하는 Entity 클래스입니다.

@Entity // 이 클래스가 DB 테이블과 연결됩니다.
@Table(name = "trades") // trades 테이블을 사용합니다.
@Getter
@NoArgsConstructor
public class Trade {
    @Id // 이 필드는 기본키 입니다. 각각의 거래를 구별하는 번호
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 를 자동으로 증가하게 해주는 용도입니다.
    private Long id;

    @Column(name = "user_id") // DB 컬럼명과 자바 필드를 연결합니다.
    private Long userId; // 컬럼명을 자바의 변수명으로 변경

    @Column(name = "stock_id")
    private Long stockId;

    @Column(name = "trade_type")
    private String tradeType; // 매수인지 매도인지

    private Integer quantity; // 거래 수량

    private BigDecimal price; // 거래 당시 가격

    @Column(name = "total_amount")
    private BigDecimal totalAmount; // 총 거래 금액

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt; // 거래 시간

    public Trade(
            Long userId,
            Long stockId,
            String tradeType,
            Integer quantity,
            BigDecimal price,
            BigDecimal totalAmount
    ) {
        this.userId = userId;
        this.stockId = stockId;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
    }
}