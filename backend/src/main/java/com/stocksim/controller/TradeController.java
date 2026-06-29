package com.stocksim.controller;

import com.stocksim.dto.TradeRequest;
import com.stocksim.dto.TradeResponse;
import com.stocksim.service.TradeService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades") // 해당 주소로 들어오는 요청을 처리하는 컨트롤러입니다.
public class TradeController {

    private  final TradeService tradeService; // 서비스 객체를 주입받습니다.

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }
    @GetMapping("/me") // 공통 주소 뒤에 /me가 붙어 GET /api/trades/me가 됩니다.
    public List<TradeResponse> findByTrades(){
        return tradeService.findMyTrades(); // 서비스의 내 거래내역 조회 로직 실행
    }
    @PostMapping ("/buy")
    public TradeResponse buy(@RequestBody TradeRequest request){ // JSON 의 BODY 를 Request로 변환 , buy 실행
        return tradeService.buy(request); //tradeResponse 로 반환 프론트로 추출
    }
}
