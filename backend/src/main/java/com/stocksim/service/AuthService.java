package com.stocksim.service;

import com.stocksim.dto.LoginRequest;
import com.stocksim.dto.LoginResponse; // 🌟 기존 String 반환에서 변경하기 위해 추가!
import com.stocksim.dto.SignUpRequest;
import com.stocksim.entity.CashHistory;
import com.stocksim.entity.User;
import com.stocksim.repository.CashHistoryRepository;
import com.stocksim.repository.UserRepository;
import com.stocksim.config.JwtTokenProvider;
import java.math.BigDecimal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final CashHistoryRepository cashHistoryRepository;

	public AuthService(UserRepository userRepository,
	                   BCryptPasswordEncoder passwordEncoder,
	                   JwtTokenProvider jwtTokenProvider,
	                   CashHistoryRepository cashHistoryRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.cashHistoryRepository = cashHistoryRepository;
	}

	@Transactional
	public void signUp(SignUpRequest request) {
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		if (userRepository.existsByNickname(request.nickname())) {
			throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		User user = new User(request.email(), encodedPassword, request.nickname());
		User savedUser = userRepository.save(user);

		BigDecimal initialCash = new BigDecimal("1000000.00");
		CashHistory signUpHistory = new CashHistory(savedUser, initialCash, "CHARGE");
		cashHistoryRepository.save(signUpHistory);
	}

	// 🌟 [변경] 반환 타입을 String에서 두 토큰이 담긴 LoginResponse로 개조합니다.
	@Transactional
	public LoginResponse login(LoginRequest request) {
		// 1. 유저 검증
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
		}

		// 2. 🌟 짧은 토큰(Access)과 긴 토큰(Refresh)을 각각 생성
		String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
		String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

		// 3. 🌟 생성한 긴 토큰은 나중을 위해 DB 유저 테이블에 보관 (더티 체킹으로 자동 업데이트)
		user.updateRefreshToken(refreshToken);

		// 4. 🌟 두 토큰을 가방(DTO)에 이쁘게 싸서 반환
		return new LoginResponse(accessToken, refreshToken);
	}

	/**
	 * 🌟 [추가] 로그아웃 비즈니스 로직
	 * 짧은 토큰(Access Token)을 받아서 검증한 뒤 해당 유저의 Refresh Token을 삭제합니다.
	 */
	@Transactional
	public void logout(String token) {
		// 1. 토큰 유효성 검증
		if (!jwtTokenProvider.validateToken(token)) {
			throw new IllegalArgumentException("유효하지 않거나 이미 만료된 토큰입니다.");
		}

		// 2. 토큰에서 이메일 추출 후 유저 조회
		String email = jwtTokenProvider.getEmail(token);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		// 3. DB에 저장되어 있던 긴 토큰(Refresh)을 완전히 비워버림(null)으로써 무효화
		user.clearRefreshToken();
	}
}