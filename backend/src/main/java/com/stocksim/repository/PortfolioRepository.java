package com.stocksim.repository;

import com.stocksim.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    // @Query("""(SQL문)""")를 사용해 커스텀 메서드 만들기 가능
    // findBy...로 시작하는 간단한 메서드는 스프링이 알아서 기능을 만들어줌.(따로 구현 필요 X)

    List<Portfolio> findByUserId(Long userId);

    @Query("""
        SELECT p 
        FROM Portfolio p 
        JOIN FETCH p.stock 
        WHERE p.user.id = :userId
    """)
    List<Portfolio> findByUserIdWithStock(@Param("userId") Long userId);

    @Query("""
        SELECT p
        FROM Portfolio p
        JOIN FETCH p.user
        JOIN FETCH p.stock
    """)
    List<Portfolio> findAllWithUserAndStock();
    Optional<Portfolio> findByUser_IdAndStock_Id(Long userId, Long stockId);
}
