package com.stocksim.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 255)
	private String email;

	@Column(nullable = false, unique = true, length = 50)
	private String nickname;

	@Column(nullable = false, length = 255)
	private String password;

	// 🌟 DB 명세의 DECIMAL(15, 2)에 맞춰 BigDecimal로 매핑
	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal cash;

	// 🌟 자동으로 생성일이 주입되도록 개선
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// 🌟 자동으로 수정일이 반영되도록 개선
	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	// JPA를 위한 기본 생성자
	public User() {
	}

	// 회원가입용 생성자 (기본 자산 설정 포함)
	public User(String email, String encodedPassword, String nickname) {
		this.email = email;
		this.password = encodedPassword;
		this.nickname = nickname;
		// 🌟 회원가입 시 초기 자본 설정 (기본값 0원, 필요시 10000000 등 모의투자금 설정 가능)
		this.cash = BigDecimal.ZERO;
	}

	// Getter 메서드들
	public Long getId() { return id; }
	public String getEmail() { return email; }
	public String getNickname() { return nickname; }
	public String getPassword() { return password; }
	public BigDecimal getCash() { return cash; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public LocalDateTime getUpdatedAt() { return updatedAt; }

	// 자산 변동(주식 매수/매도)을 위한 Setter (선택)
	public void updateCash(BigDecimal cash) {
		this.cash = cash;
	}
}