package com.stocksim.service;

import com.stocksim.dto.LoginRequest;
import com.stocksim.dto.SignUpRequest;
import com.stocksim.entity.CashHistory; // 🌟 추가
import com.stocksim.entity.User;
import com.stocksim.repository.CashHistoryRepository; // 🌟 추가
import com.stocksim.repository.UserRepository;
import com.stocksim.config.JwtTokenProvider;
import java.math.BigDecimal; // 🌟 추가
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final CashHistoryRepository cashHistoryRepository; // 🌟 1. 이력 레포지토리 필드 추가

	// 🌟 2. 생성자에 cashHistoryRepository 매개변수 추가 및 주입 완료
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
		// [검증 1] 이메일 중복 체크
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		// 🌟 [검증 2] 아까 터졌던 닉네임 중복 에러를 방지하기 위한 체크 로직 추가
		// (UserRepository에 existsByNickname이 선언되어 있어야 합니다. 없다면 아래 주석을 참고해 만들어주세요!)
		if (userRepository.existsByNickname(request.nickname())) {
			throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		// User 엔티티 생성 및 저장 (기본 cash 잔액은 1,000,000원으로 세팅된다고 가정)
		User user = new User(request.email(), encodedPassword, request.nickname());
		User savedUser = userRepository.save(user);

		// 🌟 3. 회원가입 성공 시 초기 지원금 이력(영수증) 강제 한 줄 추가
		// 금액은 오차가 없도록 BigDecimal을 사용합니다.
		BigDecimal initialCash = new BigDecimal("1000000.00");
		CashHistory signUpHistory = new CashHistory(savedUser, initialCash, "CHARGE");
		cashHistoryRepository.save(signUpHistory);
	}

	@Transactional(readOnly = true)
	public String login(LoginRequest request) {
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
		}

		return jwtTokenProvider.createToken(user.getEmail());
	}
}