package com.stocksim.service;

import com.stocksim.dto.TradeRequest;
import com.stocksim.dto.TradeResponse;
import com.stocksim.entity.Portfolio;
import com.stocksim.entity.Stock;
import com.stocksim.entity.Trade;
import com.stocksim.entity.User;
import com.stocksim.repository.PortfolioRepository;
import com.stocksim.repository.StockRepository;
import com.stocksim.repository.TradeRepository;
import com.stocksim.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service // 비즈니스 로직 담당
public class TradeService {

    private final StockRepository stockRepository;
    private  final TradeRepository tradeRepository; // 입력받은 레포지토리는 바뀌지 않는다

    //유저 가져오기
    private final UserRepository userRepository;
    //포폴 가져오기
    private final PortfolioRepository portfolioRepository;

    public TradeService(
            TradeRepository tradeRepository,
            StockRepository stockRepository,
            UserRepository userRepository,
            PortfolioRepository portfolioRepository
    ) {
        this.tradeRepository = tradeRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
    }
    //현재 로그인한 사용자 거래내역 조회
    public List<TradeResponse> findMyTrades(){
        Long userId = 1L; // 유저 아이디가 없으므로 1로 고정

        return tradeRepository.findByUserId(userId)
                .stream() // 내부의 값을 하나씩 꺼낸다
                .map(TradeResponse::from) // DTO 변환
                .toList(); // 리스트로 변환
    }
    // 구매
    public TradeResponse buy(TradeRequest request){
        return executeTrade(request,"BUY");
    }

    // 판매
    public TradeResponse sell(TradeRequest request){
        return executeTrade(request,"SELL");
    }

    // 거래 로직 매소드
    private TradeResponse executeTrade(TradeRequest request, String tradeType){

        validateQuantity(request.quantity()); //exception 거래 수량 체크

        Long userId = 1L;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Stock stock = stockRepository.findById(request.stockId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 주식입니다"));


        BigDecimal price = stock.getCurrentPrice();
        BigDecimal quantity = BigDecimal.valueOf(request.quantity());
        BigDecimal totalAmount = price.multiply(quantity);

        //포트폴리오 수량 변동 관련
        // 포트폴리오 연결(유저와 해당 유저의 주식 수량 가져오기, 해당 유저가 해당 주식이 없을경우 새로 생성)
        Portfolio portfolio = portfolioRepository
                .findByUserIdAndStockId(userId,stock.getId())
                .orElseGet(()->new Portfolio(
                        userId,
                        stock.getId(),
                        0,
                        BigDecimal.ZERO
                ));
        //거래 타입에 따라 해당 로직 실행
        if(tradeType.equals("BUY")) {
            user.decreaseCash(totalAmount);
            portfolio.buy(request.quantity(), price);
        }else if(tradeType.equals("SELL")) {
            user.increaseCash(totalAmount);
            portfolio.sell(request.quantity());
        }
        //레포지토리 저장
        portfolioRepository.save(portfolio);

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

    //exception
    private void validateQuantity(Integer quantity){
        if(quantity == null || quantity <= 0){
            throw new IllegalArgumentException("거래 수량은 1 이상이어야 합니다");
        }
    }
}
