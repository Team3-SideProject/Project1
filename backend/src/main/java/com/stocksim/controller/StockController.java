package com.stocksim.stock;

import com.stocksim.dto.StockDetailResponseDto;
import com.stocksim.dto.StockPriceResponseDto;
import com.stocksim.dto.StockResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockResponseDto>> getAllStocks() {
        List<StockResponseDto> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDetailResponseDto> getStockDetail(@PathVariable Long id) {
        try {
            StockDetailResponseDto stockDetail = stockService.getStockDetail(id);
            return ResponseEntity.ok(stockDetail);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/prices")
    public ResponseEntity<StockPriceResponseDto> getStockPrice(@PathVariable Long id) {
        try {
            StockPriceResponseDto stockPrice = stockService.getStockPrice(id);
            return ResponseEntity.ok(stockPrice);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
