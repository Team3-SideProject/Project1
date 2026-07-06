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

	// 자산 변동(주식 매수/매도)을 위한 자산 업데이트 메서드 (필요시 사용)
	public void updateCash(BigDecimal cash) {
		this.cash = cash;
	}

}