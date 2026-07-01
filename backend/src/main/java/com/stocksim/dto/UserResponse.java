package com.stocksim.dto;

import com.stocksim.entity.User;

public class UserResponse {
	private String email;
	private String name;
	private String nickname;

	public UserResponse(User user) {
		this.email = user.getEmail();
		this.name = user.getName();
		this.nickname = user.getNickname();
	}

	// 포스트맨이 JSON으로 변환할 때 Getter가 필수입니다.
	public String getEmail() { return email; }
	public String getName() { return name; }
	public String getNickname() { return nickname; }
}