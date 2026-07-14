package com.stocksim.service;

import com.stocksim.dto.PortfolioResponse;
import com.stocksim.entity.Portfolio;
import com.stocksim.entity.User;
import com.stocksim.exception.UserNotFoundException;
import com.stocksim.repository.PortfolioRepository;
import com.stocksim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public PortfolioResponse getPortfolio(String email) {

        // 1. 유저 정보 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        // 2. FETCH JOIN 메서드를 호출하여 Portfolio와 연관된 Stock을 한 번에 긁어옴 (N+1 문제 해결)
        List<Portfolio> portfolios = portfolioRepository.findByUserIdWithStock(user.getId());

        // 3. 복잡한 계산과 DTO 매핑은 PortfolioResponse 내부 메서드에 위임하여 한 줄로 끝냄
        return PortfolioResponse.of(user, portfolios);
    }
}