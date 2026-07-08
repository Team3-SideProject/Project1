package com.stocksim.dto;

// 🌟 로그인 성공 시 두 토큰을 동시에 담아줄 레코드 가방
public record LoginResponse(
		String accessToken,
		String refreshToken
) {}