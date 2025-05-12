package com.example.menuservice.repository;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.domain.Menu;
import com.example.menuservice.domain.Side;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // userUid 기반
    Optional<Cart> findByMenuAndUserUid(Menu menu, Long userUid);
    Optional<Cart> findBySideAndUserUid(Side side, Long userUid);

    // socialUid 기반
    Optional<Cart> findByMenuAndSocialUid(Menu menu, Long socialUid);
    Optional<Cart> findBySideAndSocialUid(Side side, Long socialUid);

    // 목록 조회 (사용자 기준)
    List<Cart> findByUserUid(Long userUid);
    List<Cart> findBySocialUid(Long socialUid);
}
