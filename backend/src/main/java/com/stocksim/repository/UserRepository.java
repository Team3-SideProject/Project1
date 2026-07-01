package com.stocksim.repository; // 1. 현재 레포지토리의 올바른 패키지 경로

import com.stocksim.entity.User; // 2. User 엔티티가 com.stocksim.entity에 있다고 가정하고 올바르게 수정!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 유저 찾기
    Optional<User> findByEmail(String email);
}