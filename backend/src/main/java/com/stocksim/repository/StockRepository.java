package com.stocksim.repository; // 패키지 선언 수정

import com.stocksim.entity.Stock; // Stock 엔티티 import 추가
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
