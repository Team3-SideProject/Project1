package com.stocksim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케쥴러 활성화
@SpringBootApplication
public class StocksimApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocksimApplication.class, args);
	}

}
