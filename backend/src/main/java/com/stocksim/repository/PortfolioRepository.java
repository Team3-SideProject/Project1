package com.stocksim.repository;

import com.stocksim.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    // TODO : JPA 공부해서 채워 넣기
    // @Query("""(SQL문)""")를 사용해 커스텀 메서드 만들기 가능
    // findBy...로 시작하는 간단한 메서드는 스프링이 알아서 기능을 만들어줌.(따로 구현 필요 X)
}
