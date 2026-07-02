package com.stocksim.dto;

public record LoginResponse(
		String token // 🌟 accessToken에서 token으로 명칭 변경!
) {}