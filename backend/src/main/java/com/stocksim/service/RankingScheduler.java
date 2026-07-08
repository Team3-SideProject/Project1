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

    // 자정마다 실행
    @Scheduled(cron = "0 0 0 * * ?")
    // @ManyToOne(fetch = FetchType.LAZY)가 있는 데이터 접근시 필요함
    // 1. 데이터 일관성 보장 : 메서드 실행 중 어디서든 에러가 나는 순간 전체 로직을 실행 전 상태로 되돌림
    // 2. 지연 로딩(Lazy Loading) 기능 지원 : 연관된 엔티티를 실제 필요할 때 데이터베이스에서 가져오는 지연 로딩
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

        // 자정 이전 랭킹 데이터 삭제
        rankingRepository.deleteAllInBatch();
        // 신규 랭킹 데이터 저장
        rankingRepository.saveAll(finalRankings);
    }
}