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
	private final JwtTokenProvider jwtTokenProvider;

	public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Transactional
	public void signUp(SignUpRequest request) {
		if (userRepository.findByEmail(request.email()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}

		String encodedPassword = passwordEncoder.encode(request.password());

		// 🌟 다시 request.nickname()을 사용하도록 원상복구
		User user = new User(request.email(), encodedPassword, request.nickname());
		userRepository.save(user);
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