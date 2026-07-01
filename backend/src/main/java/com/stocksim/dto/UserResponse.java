package com.stocksim.dto;

import com.stocksim.entity.User;
import java.math.BigDecimal;

public record UserResponse(
		String email,
		String nickname,
		BigDecimal cash
) {
	public UserResponse(User user) {
		this(user.getEmail(), user.getNickname(), user.getCash());
	}
}