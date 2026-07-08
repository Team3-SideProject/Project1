package com.stocksim.repository;

import com.stocksim.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    // 등수(ranking) 오름차순(1등부터 순서대로)으로 전체 조회하는 메서드
    List<Ranking> findAllByOrderByRankingAsc();
}