package com.stocksim.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		String securityJwtName = "JWT_AUTH";

		// 🌟 "입력은 토큰만, 전송은 Bearer 붙여서" 해주는 마법의 설정
		SecurityScheme securityScheme = new SecurityScheme()
				.name(securityJwtName)
				.type(SecurityScheme.Type.HTTP) // 💥 APIKEY 대신 HTTP 방식으로 지정!
				.scheme("bearer")               // 💥 핵심: 스웨거가 자동으로 앞에 "Bearer "를 붙여줍니다.
				.bearerFormat("JWT")            // 토큰 형식이 JWT임을 명시
				.in(SecurityScheme.In.HEADER);  // HTTP Header에 넣어서 전송

		return new OpenAPI()
				.info(new Info()
						.title("Stock Simulation API")
						.description("주식 시뮬레이션 프로젝트 API 문서")
						.version("1.0.0"))
				// 전역 자물쇠는 제외하고 컴포넌트만 깔끔하게 등록
				.components(new Components().addSecuritySchemes(securityJwtName, securityScheme));
	}
}