package com.stocksim.service;

import com.stocksim.config.JwtTokenProvider;
import com.stocksim.dto.TradeRequest;
import com.stocksim.dto.TradeResponse;
import com.stocksim.entity.Portfolio;
import com.stocksim.entity.Stock;
import com.stocksim.entity.Trade;
import com.stocksim.entity.User;
import com.stocksim.entity.CashHistory;
import com.stocksim.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final JwtTokenProvider jwtTokenProvider; // jwt 연결

    private final CashHistoryRepository cashHistoryRepository; // cashHistory 연결

    public TradeService(
            TradeRepository tradeRepository,
            StockRepository stockRepository,
            UserRepository userRepository,
            PortfolioRepository portfolioRepository,
            JwtTokenProvider jwtTokenProvider,
             CashHistoryRepository cashHistoryRepository
            ) {
        this.tradeRepository = tradeRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cashHistoryRepository = cashHistoryRepository;
    }

    //현재 로그인한 사용자 거래내역 조회
    public List<TradeResponse> findMyTrades(String authorization) {
        User user = getLoggedUser(authorization);
        return tradeRepository.findByUserId(user.getId())
                .stream() // 내부의 값을 하나씩 꺼낸다
                .map(TradeResponse::from) // DTO 변환
                .toList(); // 리스트로 변환
    }
    // 구매
    @Transactional //하나의 작업을 수행하기 위해 실행되는 여러 작업을 하나의 단위로 묶어, 모두 성공하거나 모두 실패하도록 보장하는 것
    public TradeResponse buy(TradeRequest request, String authorization) {
        return executeTrade(request,"BUY", authorization);
    }

    // 판매
    @Transactional
    public TradeResponse sell(TradeRequest request,  String authorization) {
        return executeTrade(request,"SELL",  authorization);
    }

    // 거래 로직 매소드
    private TradeResponse executeTrade(TradeRequest request, String tradeType, String authorization){

        validateQuantity(request.quantity()); //exception 거래 수량 체크

        User user = getLoggedUser(authorization);
        Long userId = user.getId();

        Stock stock = stockRepository.findById(request.stockId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 주식입니다"));

        BigDecimal price = stock.getCurrentPrice();
        BigDecimal quantity = BigDecimal.valueOf(request.quantity());
        BigDecimal totalAmount = price.multiply(quantity);

        //거래 타입에 따라 해당 로직 실행
        if(tradeType.equals("BUY")) {
            Portfolio portfolio = portfolioRepository
                    .findByUserIdAndStockId(userId,stock.getId()) //포트폴리오 수량 변동 관련
                    .orElseGet(()->new Portfolio(      // 포트폴리오 연결(유저와 해당 유저의 주식 수량 가져오기, 해당 유저가 해당 주식이 없을경우 새로 생성)
                            userId,
                            stock.getId(),
                            0,
                            BigDecimal.ZERO
                    ));
            user.decreaseCash(totalAmount);
            portfolio.buy(request.quantity(), price);
            CashHistory cashHistory = new CashHistory(
                    user,
                    totalAmount.negate(),
                    "BUY"
            );
            cashHistoryRepository.save(cashHistory);
            portfolioRepository.save(portfolio);
        }else if(tradeType.equals("SELL")) {
            Portfolio portfolio = portfolioRepository
                    .findByUserIdAndStockId(userId,stock.getId())
                    .orElseThrow(()->new IllegalArgumentException("보유하지 않은 주식입니다"));
            portfolio.sell(request.quantity());
            user.increaseCash(totalAmount);
            CashHistory cashHistory = new CashHistory(
                    user,
                    totalAmount,
                    "SELL"
            );
            cashHistoryRepository.save(cashHistory);
            portfolioRepository.save(portfolio);
        }
        //레포지토리 저장
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

    private User getLoggedUser(String authorization){
        if(authorization == null || !authorization.startsWith("Bearer ")){
            throw new IllegalArgumentException("Authorization 헤더 형식이 올바르지 않습니다");
        }
        String token = authorization.substring(7);
        if(!jwtTokenProvider.validateToken(token)){
            throw new IllegalArgumentException("유효하지 않은 토큰 입니다");
        }
        String email = jwtTokenProvider.getEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
    }
}
