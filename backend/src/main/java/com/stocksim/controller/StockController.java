package com.stocksim.controller;

import com.stocksim.dto.StockResponse;
import com.stocksim.service.StockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks") // 해당 주소로 들어오는 요청을 처리하는 클래스 입니다.
public class StockController {

    private final StockService stockService; // Controller가 비즈니스 로직을 수행하기 위해 Service를 주입받습니다.

    public StockController(StockService stockService) {
        this.stockService = stockService;
    } // 생성자를 통해 Service 객체를 주입받습니다.

    @GetMapping
    public List<StockResponse> findAll() {
        return stockService.findAll();
    }// Service에게 전체 주식 조회를 요청하고 결과를 JSON으로 반환합니다.
}