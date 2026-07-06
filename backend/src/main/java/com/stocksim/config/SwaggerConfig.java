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
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);

        // 🌟 핵심 변경: 스웨거가 자동으로 Bearer를 붙이지 않고, 사용자가 입력한 그대로 'Header'에 꽂아주도록 설정
        Components components = new Components().addSecuritySchemes(securityJwtName,
                new SecurityScheme()
                        .name("Authorization")             // 🌟 컨트롤러가 기다리는 헤더 이름 딱 지정
                        .type(SecurityScheme.Type.APIKEY)  // 🌟 APIKEY 방식으로 변경
                        .in(SecurityScheme.In.HEADER));    // 헤더에 담아 전송

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}