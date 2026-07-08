package com.stocksim.service;

import com.stocksim.entity.Stock;
import com.stocksim.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j // 클래스 내에서 로깅을 위한 인스턴스를 자동생성해줌
public class StockPriceSchedulerService {

    private final StockRepository stockRepository;
    private final Random random = new Random();

    // 30분마다 실행되는 방식 스프링의 스케줄링 기능을 사용하여 백그라운드에서 자동 실행하게함
    // fixedRate는 이전 작업의 시작 시간부터 다음 작업의 시작 시간까지의 간격을 밀리초 단위로 지정함
    @Scheduled(fixedRate = 300000)
    @Transactional // 가격 업데이트는 트랜잭션 내에서 이루어져야 함 주식의 모든 가격이 업데이트를 성공해야만 데이터베이스에 반영되고, 오류 발생시 모든 사항이 롤백됨.
    public void updateStockPrices() {
        log.info("주식 가격 업데이트 시작");
        List<Stock> stocks = stockRepository.findAll();
        //확인용 로그 출력
        for (Stock stock : stocks) {
            BigDecimal currentPrice = stock.getCurrentPrice();
            // -5% ~ +5% 범위 내에서 가격 변동 임시 테스트용 기준
            double changePercentage = (random.nextDouble() * 0.1) - 0.05; // -0.05 ~ +0.05
            BigDecimal priceChange = currentPrice.multiply(BigDecimal.valueOf(changePercentage));
            BigDecimal newPrice = currentPrice.add(priceChange).setScale(2, RoundingMode.HALF_UP);

            // 가격이 0 이하로 떨어지지 않도록 방지하는 코드
            if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                newPrice = BigDecimal.valueOf(0.01); // 최소 가격 설정
            }

            stock.setCurrentPrice(newPrice);
            stockRepository.save(stock); // 변경된 가격 저장
            log.debug("Stock {} ({}) price updated from {} to {}", stock.getName(), stock.getCode(), currentPrice, newPrice);
        }
        log.info("주식 가격 업데이트 완료.");
    }
}
