package com.stocksim.service;

import com.stocksim.dto.StockResponse;
import com.stocksim.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService { // 비즈니스 로직을 담당하는 곳 입니다.

    private final StockRepository stockRepository; // repository 에서 DB 를 접근

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }// service 내부의 객체 형태 변화 로직을 작동

    public List<StockResponse> findAll() { // 변환된 객체형태를 리스트에 담는 과정
        return stockRepository.findAll()
                .stream()
                .map(StockResponse::from)
                .toList();
    }
}