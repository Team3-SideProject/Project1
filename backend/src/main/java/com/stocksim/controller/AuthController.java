package com.stocksim.controller;

import com.stocksim.config.JwtTokenProvider;
import com.stocksim.dto.LoginRequest;
import com.stocksim.dto.LoginResponse;
import com.stocksim.dto.SignUpRequest;
import com.stocksim.dto.UserResponse;
import com.stocksim.dto.CashHistoryResponse;
import com.stocksim.entity.CashHistory;
import com.stocksim.entity.User;
import com.stocksim.repository.CashHistoryRepository;
import com.stocksim.repository.UserRepository;
import com.stocksim.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final CashHistoryRepository cashHistoryRepository;

	public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, CashHistoryRepository cashHistoryRepository) {
		this.authService = authService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userRepository = userRepository;
		this.cashHistoryRepository = cashHistoryRepository;
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody SignUpRequest request) {
		try {
			authService.signUp(request);
			return ResponseEntity.ok("회원가입이 완료되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("에러: " + e.getMessage());
		}
	}

	// 🌟 [변경 점 1] LoginResponse 구조 변경에 따른 리팩토링
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			// 원래는 여기서 String token을 받았지만, 이제 서비스가 LoginResponse(가방)를 바로 줍니다.
			LoginResponse response = authService.login(request);

			// 가방을 그대로 손님(스웨거)에게 전달합니다. (안에 짧은 거, 긴 거 다 들어있음)
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// 🌟 [추가 점 2] 로그아웃 API 새로 도입!
	@Operation(summary = "로그아웃", security = @SecurityRequirement(name = "JWT_AUTH"))
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
		// 1. 토큰이 잘 들어왔나 서빙 상태 확인
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 존재하지 않습니다.");
		}

		String token = bearerToken.substring(7);

		try {
			// 2. 주방(Service)으로 가서 토큰을 지우는 핵심 로직 수행
			authService.logout(token);
			return ResponseEntity.ok("로그아웃이 완료되었습니다.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

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

	@Operation(summary = "내 자산 변동 이력 조회", security = @SecurityRequirement(name = "JWT_AUTH"))
	@GetMapping("/histories")
	public ResponseEntity<?> getMyHistories(@RequestHeader(value = "Authorization", required = false) String bearerToken) {
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

		List<CashHistoryResponse> responses = cashHistoryRepository.findByUserOrderByIdDesc(user)
				.stream()
				.map(CashHistoryResponse::new)
				.toList();

		return ResponseEntity.ok(responses);
	}
}