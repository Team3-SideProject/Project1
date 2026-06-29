package com.stocksim.repository;

import com.stocksim.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade,Long> { // JPA 가 제공하는 기본 DB 기능을 사용
    // repository는 DB 접근 전용 인터페이스 입니다.
    // Trade 엔티티를 관리하고, 기본키 타입은 Long
    List<Trade> findByUserId(Long userId); // userId 에 해당하는 거래내역을 조회
}

