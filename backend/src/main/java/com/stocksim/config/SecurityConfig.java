package com.stocksim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 설정을 활성화합니다.
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 이 설정을 안 해주면 모든 요청이 다 차단됩니다.
	// 지금은 개발 단계이므로 일단 모든 URL(/api/auth/signup 등) 접근을 허용(anyRequest().permitAll())해 줍니다.
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable()) // 테스트를 위해 CSRF 보안 방식을 잠시 끕니다.
				.authorizeHttpRequests(auth -> auth
						.anyRequest().permitAll() // 모든 요청은 로그인 없이 접근 허용!
				);
		return http.build();
	}
}