package com.stocksim.service;

import com.stocksim.dto.TradeResponse;
import com.stocksim.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 비즈니스 로직 담당
public class TradeService {

    private  final TradeRepository tradeRepository; // 입력받은 레포지토리는 바뀌지 않는다
    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }
    //현재 로그인한 사용자 거래내역 조회
    public List<TradeResponse> findMyTrades(){
        Long userId = 1L; // 유저 아이디가 없으므로 1로 고정

        return tradeRepository.findByUserId(userId)
                .stream() // 내부의 값을 하나씩 꺼낸다
                .map(TradeResponse::from) // DTO 변환
                .toList(); // 리스트로 변환
    }
}
