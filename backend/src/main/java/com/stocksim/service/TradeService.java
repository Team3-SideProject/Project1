package com.stocksim.service;

import com.stocksim.dto.TradeRequest;
import com.stocksim.dto.TradeResponse;
import com.stocksim.entity.Stock;
import com.stocksim.entity.Trade;
import com.stocksim.repository.StockRepository;
import com.stocksim.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service // 비즈니스 로직 담당
public class TradeService {

    private final StockRepository stockRepository;
    private  final TradeRepository tradeRepository; // 입력받은 레포지토리는 바뀌지 않는다
    public TradeService(
            TradeRepository tradeRepository,
            StockRepository stockRepository
    ) {
        this.tradeRepository = tradeRepository;
        this.stockRepository = stockRepository;
    }
    //현재 로그인한 사용자 거래내역 조회
    public List<TradeResponse> findMyTrades(){
        Long userId = 1L; // 유저 아이디가 없으므로 1로 고정

        return tradeRepository.findByUserId(userId)
                .stream() // 내부의 값을 하나씩 꺼낸다
                .map(TradeResponse::from) // DTO 변환
                .toList(); // 리스트로 변환
    }
    // 구매 로직
    public TradeResponse buy(TradeRequest request){
        Long userId = 1L;
        Stock stock = stockRepository.findById(request.stockId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주식입니다")); // 요청받은 stock id 로 주식 조회
        BigDecimal price = stock.getCurrentPrice(); // 주식 가격 가져오기
        BigDecimal quantity = BigDecimal.valueOf(request.quantity()); // 구매 수량 가져오기
        BigDecimal totalAmount = price.multiply(quantity); // 총 거래 금액 계산

        Trade trade = new Trade( // 거래 객체 생성
                userId,
                stock.getId(),
                "BUY",
                request.quantity(),
                price,
                totalAmount
                );
        Trade savedTrade = tradeRepository.save(trade); // DB 저장
        return TradeResponse.from(savedTrade); //DTO 반환
    }
    // 판매 로직
    public TradeResponse sell(TradeRequest request){
        Long userId = 1L;

        Stock stock = stockRepository.findById(request.stockId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 주식입니다"));

        BigDecimal price = stock.getCurrentPrice();
        BigDecimal quantity = BigDecimal.valueOf(request.quantity());
        BigDecimal totalAmount = price.multiply(quantity);

        Trade trade = new Trade(
                userId,
                stock.getId(),
                "SELL",
                request.quantity(),
                price,
                totalAmount
        );
        Trade savedTrade = tradeRepository.save(trade);
        return TradeResponse.from(savedTrade);
    }
}
