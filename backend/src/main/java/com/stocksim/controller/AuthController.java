package com.stocksim.controller;

import com.stocksim.config.JwtTokenProvider;
import com.stocksim.dto.LoginRequest;
import com.stocksim.dto.LoginResponse;
import com.stocksim.dto.SignUpRequest;
import com.stocksim.dto.UserResponse;
import com.stocksim.entity.User;
import com.stocksim.repository.UserRepository;
import com.stocksim.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository; // 유저 조회를 위해 리포지토리 주입

	public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
		this.authService = authService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
		try {
			authService.signUp(request);
			return ResponseEntity.ok("회원가입이 완료되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			String token = authService.login(request);
			return ResponseEntity.ok(new LoginResponse(token));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 🌟 내 정보 조회 API 추가
	@GetMapping("/me")
	public ResponseEntity<?> getMe(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
		// 1. 헤더에 토큰이 없거나 Bearer 로 시작하지 않으면 차단
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 존재하지 않습니다.");
		}

		// 2. "Bearer " 뒷부분의 순수한 토큰 문자열만 잘라냄
		String token = bearerToken.substring(7);

		// 3. 토큰이 진짜인지 검증
		if (!jwtTokenProvider.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
		}

		// 4. 토큰이 진짜라면 내부에서 이메일 추출
		String email = jwtTokenProvider.getEmail(token);

		// 5. 이메일로 DB에서 유저를 찾아 DTO에 담아 반환
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		return ResponseEntity.ok(new UserResponse(user));
	}
}