package com.stocksim.dto;

public record LoginRequest(
		String email,
		String password
) {}