package com.stocksim.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_histories")
public class CashHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 어떤 유저의 돈인지 연결 (N:1 연관관계)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// 자산 변화량 (양수: 입금/매도, 음수: 출금/매수)
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;

	// 거래 종류 (예: "BUY", "SELL", "CHARGE")
	@Column(nullable = false)
	private String type;

	// 거래 일시
	@Column(nullable = false)
	private LocalDateTime createdAt;

	// JPA용 기본 생성자
	protected CashHistory() {}

	// 실제 서비스에서 사용할 생성자
	public CashHistory(User user, BigDecimal amount, String type) {
		this.user = user;
		this.amount = amount;
		this.type = type;
		this.createdAt = LocalDateTime.now();
	}

	// Getter들 (Record DTO로 변환할 때 값 꺼내기용)
	public Long getId() { return id; }
	public User getUser() { return user; }
	public BigDecimal getAmount() { return amount; }
	public String getType() { return type; }
	public LocalDateTime getCreatedAt() { return createdAt; }
}