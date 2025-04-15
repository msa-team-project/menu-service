package com.example.menuservice.repository;

import com.example.menuservice.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 세션 기반 장바구니 조회
    List<Cart> findBySessionId(String sessionId);

    // 로그인 사용자 장바구니 조회
    List<Cart> findByUserUid(Long userUid);

    // 소셜 로그인 사용자 장바구니 조회
    List<Cart> findBySocialUid(Long socialUid);

    // 장바구니 비우기
    void deleteBySessionId(String sessionId);
    void deleteByUserUid(Long userUid);
    void deleteBySocialUid(Long socialUid);


}
