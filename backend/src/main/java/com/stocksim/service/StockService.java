package com.stocksim.stock;

import com.stocksim.entity.Stock;
import com.stocksim.dto.StockDetailResponseDto;
import com.stocksim.dto.StockPriceResponseDto;
import com.stocksim.dto.StockResponseDto;
import com.stocksim.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository stockRepository;

    public List<StockResponseDto> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(StockResponseDto::from)
                .collect(Collectors.toList());
    }

    public StockDetailResponseDto getStockDetail(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Stock not found with id: " + id));
        return StockDetailResponseDto.from(stock);
    }

    public StockPriceResponseDto getStockPrice(Long id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Stock not found with id: " + id));
        return StockPriceResponseDto.from(stock);
    }
}
