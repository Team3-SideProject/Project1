package com.stocksim.dto;

import java.math.BigDecimal;

public record RankingResponse(
        Long userId,
        String nickname,
        BigDecimal totalAsset,
        int ranking
) {

}