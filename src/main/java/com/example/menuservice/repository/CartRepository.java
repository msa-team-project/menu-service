package com.example.menuservice.repository;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.domain.CustomCart;
import com.example.menuservice.domain.Menu;
import com.example.menuservice.domain.Side;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Cart 단건 조회
    Optional<Cart> findById(Long id);

    // Cart 삭제
    void deleteById(Long id);

    Optional<Cart> findByMenu(Menu menu);

    Optional<Cart> findBySide(Side side);

    Optional<Cart> findByCustomCart(CustomCart customCart);

}
