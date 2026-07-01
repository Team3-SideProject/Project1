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

	// 🌟 다시 nickname 필드로 복구
	@Column(nullable = false, unique = true, length = 50)
	private String nickname;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal cash;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	public User() {
	}

	// 회원가입용 생성자
	public User(String email, String encodedPassword, String nickname) {
		this.email = email;
		this.password = encodedPassword;
		this.nickname = nickname;
		this.cash = BigDecimal.ZERO;
	}

	public Long getId() { return id; }
	public String getEmail() { return email; }
	public String getNickname() { return nickname; } // 🌟 복구
	public String getPassword() { return password; }
	public BigDecimal getCash() { return cash; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public LocalDateTime getUpdatedAt() { return updatedAt; }

	public void updateCash(BigDecimal cash) {
		this.cash = cash;
	}
}