package com.stocksim.service;

import com.stocksim.dto.LoginRequest;
import com.stocksim.dto.SignUpRequest;
import com.stocksim.entity.User;
import com.stocksim.repository.UserRepository;
import com.stocksim.config.JwtTokenProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider; // 토큰 제공자 추가

	public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Transactional
	public void signUp(SignUpRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		User user = new User(request.getEmail(), encodedPassword, request.getName(), request.getNickname());
		userRepository.save(user);
	}

	// 🌟 로그인 로직 추가
	@Transactional(readOnly = true)
	public String login(LoginRequest request) {
		// 1. 이메일로 유저 찾기
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

		// 2. 비밀번호가 일치하는지 비교 (BCrypt 매칭 함수 사용!)
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
		}

		// 3. 일치하면 JWT 토큰을 발행해서 반환
		return jwtTokenProvider.createToken(user.getEmail());
	}
}