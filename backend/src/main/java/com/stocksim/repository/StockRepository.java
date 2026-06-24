package com.stocksim.repository;

import com.stocksim.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> { // 이 한줄을 통하여 기본 DB 기능이 생깁니다
}