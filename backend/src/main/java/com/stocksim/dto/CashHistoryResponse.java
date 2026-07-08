package com.stocksim.dto;

import com.stocksim.entity.CashHistory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashHistoryResponse(
		Long id,
		BigDecimal amount,
		String type,
		LocalDateTime createdAt
) {
	// 엔티ti 객체를 넣으면 자동으로 레코드 DTO 가방으로 쏙 변환해주는 편의 생성자
	public CashHistoryResponse(CashHistory history) {
		this(
				history.getId(),
				history.getAmount(),
				history.getType(),
				history.getCreatedAt()
		);
	}
}