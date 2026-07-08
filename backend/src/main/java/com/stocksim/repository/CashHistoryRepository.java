package com.stocksim.repository;

import com.stocksim.entity.CashHistory;
import com.stocksim.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CashHistoryRepository extends JpaRepository<CashHistory, Long> {
	// 특정 유저의 이력을 최신순(Id 역순)으로 정렬해서 가져오는 메서드
	List<CashHistory> findByUserOrderByIdDesc(User user);
}