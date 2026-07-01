package com.stocksim.dto;

public record SignUpRequest(
		String email,
		String password,
		String name,
		String nickname
) {}