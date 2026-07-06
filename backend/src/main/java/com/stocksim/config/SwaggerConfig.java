package com.stocksim.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		String securityJwtName = "JWT_AUTH";

		// ❌ 기존에 있던 .addSecurityItem(securityRequirement)을 제거합니다!
		// 전역 설정을 빼버리면 기본적으로 모든 API의 자물쇠가 사라집니다.

		Components components = new Components().addSecuritySchemes(securityJwtName,
				new SecurityScheme()
						.name("Authorization")
						.type(SecurityScheme.Type.APIKEY)
						.in(SecurityScheme.In.HEADER));

		return new OpenAPI().components(components);
	}
}