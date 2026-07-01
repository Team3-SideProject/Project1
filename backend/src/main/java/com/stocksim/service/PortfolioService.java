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
                portfolioRepository.findByUserId(userId);

        List<PortfolioStockResponse> stocks = new ArrayList<>();

        BigDecimal totalStockAsset = BigDecimal.ZERO;
        BigDecimal totalPurchase = BigDecimal.ZERO;

        for (Portfolio portfolio : portfolios) {

            Stock stock = stockRepository.findById(portfolio.getStockId())
                    .orElseThrow();

            BigDecimal currentValue =
                    stock.getCurrentPrice()
                            .multiply(BigDecimal.valueOf(portfolio.getQuantity()));

            BigDecimal purchaseValue =
                    portfolio.getAverageBuyPrice()
                            .multiply(BigDecimal.valueOf(portfolio.getQuantity()));

            stocks.add(
                    new PortfolioStockResponse(
                            stock.getCode(),
                            stock.getName(),
                            portfolio.getQuantity(),
                            portfolio.getAverageBuyPrice(),
                            stock.getCurrentPrice(),
                            currentValue
                    )
            );

            totalStockAsset = totalStockAsset.add(currentValue);
            totalPurchase = totalPurchase.add(purchaseValue);
        }

        BigDecimal totalAsset =
                user.getCash().add(totalStockAsset);

        BigDecimal totalProfit =
                totalAsset.subtract(totalPurchase);

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
