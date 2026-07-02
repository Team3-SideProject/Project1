package com.stocksim.service;

import com.stocksim.dto.PortfolioResponse;
import com.stocksim.dto.PortfolioStockResponse;
import com.stocksim.entity.Portfolio;
import com.stocksim.entity.Stock;
import com.stocksim.entity.User;
import com.stocksim.repository.PortfolioRepository;
import com.stocksim.repository.StockRepository;
import com.stocksim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public PortfolioResponse getPortfolio(String email) {

        // TODO: 포트폴리오 조회 로직 구현
        // 특정 유저의 총 자산(포트폴리오) : cash + (특정 stock의 갯수*그 stock의 현재 가)

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Long userId = user.getId();

        List<Portfolio> portfolios =
                portfolioRepository.findByUserId(userId); // 특정유저의 특정주식 포트폴리오 정보 전부 가져와서 리스트에 담기

        List<PortfolioStockResponse> stocks = new ArrayList<>(); // 자식DTO 정보 담을 리스트 생성

        BigDecimal totalStockAsset = BigDecimal.ZERO;
        BigDecimal totalPurchase = BigDecimal.ZERO;

        for (Portfolio portfolio : portfolios) {

            Stock stock = stockRepository.findById(portfolio.getStockId())
                    .orElseThrow(); // 포트폴리오의 주식id로 주식 정보 가져오기

            BigDecimal currentValue =
                    stock.getCurrentPrice()
                            .multiply(BigDecimal.valueOf(portfolio.getQuantity())); // 현재가 × 보유 수량

            BigDecimal purchaseValue =
                    portfolio.getAverageBuyPrice()
                            .multiply(BigDecimal.valueOf(portfolio.getQuantity())); // 평균 매수가 × 보유 수량

            stocks.add(
                    new PortfolioStockResponse(
                            stock.getCode(),
                            stock.getName(),
                            portfolio.getQuantity(),
                            portfolio.getAverageBuyPrice(),
                            stock.getCurrentPrice(),
                            currentValue
                    )
            ); // 자식 DTO 리스트에 자식 DTO add

            totalStockAsset = totalStockAsset.add(currentValue);
            totalPurchase = totalPurchase.add(purchaseValue);
        }

        BigDecimal totalAsset =
                user.getCash().add(totalStockAsset); // 현금 + 보유 주식 평가금액

        BigDecimal totalProfit =
                totalAsset.subtract(totalPurchase); // 현재 자산 - 내가 투자한 금액

        return new PortfolioResponse(
                user.getId(),
                user.getNickname(),
                totalAsset,
                totalPurchase,
                totalProfit,
                stocks
        );
    }
}
