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
        // 🌟 핵심 변경: 스웨거가 자동으로 Bearer를 붙이지 않고, 사용자가 입력한 그대로 'Header'에 꽂아주도록 설정
        Components components = new Components().addSecuritySchemes(securityJwtName,
                new SecurityScheme()
                        .name("Authorization")             // 🌟 컨트롤러가 기다리는 헤더 이름 딱 지정
                        .type(SecurityScheme.Type.APIKEY)  // 🌟 APIKEY 방식으로 변경
                        .in(SecurityScheme.In.HEADER));    // 헤더에 담아 전송

	@Bean
	public OpenAPI openAPI() {
		// 🌟 스웨거 자물쇠를 누르면 기본적으로 모든 API에 토큰 헤더가 적용되도록 명시
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT_AUTH");

		// 🌟 "JWT_AUTH"라는 이름의 보안 규격을 정의 (Http Bearer 방식 지정)
		SecurityScheme securityScheme = new SecurityScheme()
				.name("JWT_AUTH")
				.type(SecurityScheme.Type.HTTP) // HTTP 프로토콜 방식
				.scheme("bearer")               // 💥 핵심: 자동으로 앞에 "Bearer "를 붙여줍니다!
				.bearerFormat("JWT")            // 토큰 형식이 JWT임을 명시
				.in(SecurityScheme.In.HEADER);  // HTTP Header에 넣어서 전송

		return new OpenAPI()
				.info(new Info()
						.title("Stock Simulation API")
						.description("주식 시뮬레이션 프로젝트 API 문서")
						.version("1.0.0"))
				//.addSecurityItem(securityRequirement)
				.components(new Components().addSecuritySchemes("JWT_AUTH", securityScheme));
	}
}