package com.stocksim.controller;

import com.stocksim.config.JwtTokenProvider;
import com.stocksim.dto.LoginRequest;
import com.stocksim.dto.LoginResponse;
import com.stocksim.dto.SignUpRequest;
import com.stocksim.dto.UserResponse;
import com.stocksim.entity.User;
import com.stocksim.repository.UserRepository;
import com.stocksim.service.AuthService;
// 🌟 1. 스웨거 어노테이션 사용을 위한 임포트 추가
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
		} catch (Exception e) {
			// 🌟 여기에 걸려서 스웨거 화면에 진짜 에러 원인 글자가 찍힐 것입니다!
			return ResponseEntity.status(500).body("에러: " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		// 🔓 자물쇠 없음: 토큰 없이 누구나 접근 가능
		try {
			String token = authService.login(request);
			return ResponseEntity.ok(new LoginResponse(token));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 🌟 2. 이 메서드에만 콕 집어서 자물쇠를 채우는 핵심 코드 추가!
	// SwaggerConfig에서 정의한 이름인 "JWT_AUTH"와 매핑됩니다.
	@Operation(summary = "내 정보 조회", security = @SecurityRequirement(name = "JWT_AUTH"))
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