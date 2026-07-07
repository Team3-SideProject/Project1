package com.stocksim.service;

import com.stocksim.entity.User;
import com.stocksim.entity.Portfolio;
import com.stocksim.entity.Ranking;
import com.stocksim.repository.UserRepository;
import com.stocksim.repository.PortfolioRepository;
import com.stocksim.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingScheduler {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final RankingRepository rankingRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void calculateTotalAssetRanking() {
        LocalDateTime calculatedAt = LocalDateTime.now();

        List<User> users = userRepository.findAll();
        List<Portfolio> allPortfolios = portfolioRepository.findAllWithUserAndStock();

        // key : 유저 아이디
        // value : 해당 유저의 포트폴리오 리스트
        Map<Long, List<Portfolio>> portfolioMap = allPortfolios.stream()
                .collect(Collectors.groupingBy(p -> p.getUser().getId()));

        List<Ranking> temporaryRankings = new ArrayList<>();

        for (User user : users) {

            List<Portfolio> portfolios =
                    portfolioMap.getOrDefault(user.getId(), Collections.emptyList());

            BigDecimal stockAsset = BigDecimal.ZERO;

            for (Portfolio portfolio : portfolios) {
                stockAsset = stockAsset.add(
                        portfolio.getStock()
                                .getCurrentPrice()
                                .multiply(BigDecimal.valueOf(portfolio.getQuantity()))
                );
            }

            BigDecimal totalAsset = user.getCash().add(stockAsset);

            temporaryRankings.add(
                    new Ranking(
                            0,
                            user.getId(),
                            user.getNickname(),
                            totalAsset,
                            calculatedAt
                    )
            );
        }

        temporaryRankings.sort(
                Comparator.comparing(Ranking::getTotalAsset).reversed()
        );

        List<Ranking> finalRankings = new ArrayList<>();

        int rank = 1;

        for (int i = 0; i < temporaryRankings.size(); i++) {

            Ranking current = temporaryRankings.get(i);

            if (i > 0 &&
                    current.getTotalAsset().compareTo(
                            temporaryRankings.get(i - 1).getTotalAsset()) != 0) {
                rank = i + 1;
            }

            finalRankings.add(
                    new Ranking(
                            rank,
                            current.getUserId(),
                            current.getNickname(),
                            current.getTotalAsset(),
                            calculatedAt
                    )
            );
        }

        rankingRepository.deleteAllInBatch();
        rankingRepository.saveAll(finalRankings);
    }
}