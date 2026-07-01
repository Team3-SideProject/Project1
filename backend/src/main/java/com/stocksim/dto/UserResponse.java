package com.stocksim.dto;

import com.stocksim.entity.User;

public record UserResponse(
		String email,
		String name,
		String nickname
) {
	public UserResponse(User user) {
		this(user.getEmail(), user.getName(), user.getNickname());
	}
}