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
        return executeTrade(request,"BUY");
    }
    // 판매 로직
    public TradeResponse sell(TradeRequest request){
        return executeTrade(request,"SELL");
    }
    // 거래 로직 매소드 분리
    private TradeResponse executeTrade(TradeRequest request, String tradeType){
        //exception
        validateQuantity(request.quantity());

        Long userId = 1L;

        Stock stock = stockRepository.findById(request.stockId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 주식입니다"));


        BigDecimal price = stock.getCurrentPrice();
        BigDecimal quantity = BigDecimal.valueOf(request.quantity());
        BigDecimal totalAmount = price.multiply(quantity);

        Trade trade = new Trade(
                userId,
                stock.getId(),
                tradeType,
                request.quantity(),
                price,
                totalAmount
        );
        Trade savedTrade = tradeRepository.save(trade);
        return TradeResponse.from(savedTrade);
    }
    private void validateQuantity(Integer quantity){
        //exception
        if(quantity == null || quantity <= 0){
            throw new IllegalArgumentException("거래 수량은 1 이상이어야 합니다");
        }
    }
}
