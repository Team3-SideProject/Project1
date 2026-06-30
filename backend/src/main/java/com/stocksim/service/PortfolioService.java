package com.stocksim.service;

import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    public void getPortfolio() {
        // TODO: 포트폴리오 조회 로직 구현
        // 특정 유저의 총 자산(포트폴리오) : cash + (특정 stock의 갯수*그 stock의 현재 가)
        // average_buy_price : stock_price_histories에서 특정 stock 샀을 때 가격 평균
        // list로 넘겨야하나?
    }
}
