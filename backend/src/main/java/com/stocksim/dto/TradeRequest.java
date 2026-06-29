package com.stocksim.dto;

public record TradeRequest(
    Long stockId,
    Integer quantity
){
}
