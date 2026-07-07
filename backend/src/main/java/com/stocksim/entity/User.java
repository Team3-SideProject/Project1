package com.stocksim.entity;

import jakarta.persistence.*; // JPA
import lombok.Getter; // getter 메서드 자동 생성
import lombok.NoArgsConstructor; // 기본 생성자 자동 생성

import java.math.BigDecimal;
import java.time.LocalDateTime;

// 이 코드는 DB 에서 자바로 데이터를 가져와 변환 해주는 코드입니다.
// users 테이블을 자바 객체로 매핑하는 Entity 클래스입니다.
@Entity // 이 클래스가 DB 테이블과 연결됩니다.
@Table(name = "users") // users 테이블을 사용합니다.
@Getter
@NoArgsConstructor
public class User {

	@Id // 이 필드는 기본키 입니다. 각각의 유저를 구별하는 번호
	@GeneratedValue(strategy = GenerationType.IDENTITY) // id 를 자동으로 증가하게 해주는 용도입니다.
	private Long id;

	private String email;

	private String nickname;

	private String password;

	private BigDecimal cash;

	// 🌟 추가: 로그인 유지를 위한 긴 토큰 (JWT 문자열이 길기 때문에 길이를 500으로 여유 있게 설정)
	@Column(name = "refresh_token", length = 500)
	private String refreshToken;

	// 🌟 Trade 엔티티 스타일과 동일하게 DB 기본 설정을 따르도록 세팅
	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt; // 생성일

	// 🌟 수정일 역시 DB의 ON UPDATE CURRENT_TIMESTAMP 규칙을 따르도록 세팅
	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt; // 수정일

	// 회원가입용 생성자
	public User(String email, String encodedPassword, String nickname) {
		this.email = email;
		this.password = encodedPassword;
		this.nickname = nickname;
		this.cash = new BigDecimal("1000000"); // 초기 보유 현금 100만 설정
	}

	// ================= 비즈니스 로직 메서드 ================= //

	// 자산 변동(주식 매수/매도)을 위한 자산 업데이트 메서드
	public void updateCash(BigDecimal amount) {
		// 현재 잔액 = 현재 잔액 + (변화량)
		// 변화량이 음수(-500,000)면 알아서 빼지고, 양수(+600,000)면 알아서 더해집니다!
		this.cash = this.cash.add(amount);
	}

	// 🌟 추가: 로그인 성공 시 긴 토큰을 업데이트하는 메서드
	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	// 🌟 추가: 로그아웃 시 긴 토큰을 비워버리는 메서드
	public void clearRefreshToken() {
		this.refreshToken = null;
	}
}