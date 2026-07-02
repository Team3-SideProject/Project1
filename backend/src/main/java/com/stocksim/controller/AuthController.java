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
	private final UserRepository userRepository;

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
			// 🌟 프론트엔드의 요청대로 키 이름이 token으로 반환되도록 Response 객체 생성
			return ResponseEntity.ok(new LoginResponse(token));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getMe(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 존재하지 않습니다.");
		}

		String token = bearerToken.substring(7);

		if (!jwtTokenProvider.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
		}

		String email = jwtTokenProvider.getEmail(token);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		return ResponseEntity.ok(new UserResponse(user));
	}
}