package com.example.menuservice.service;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.domain.Menu;
import com.example.menuservice.repository.CartRepository;
import com.example.menuservice.repository.MenuRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    // 장바구니에 메뉴 추가
    public void addToCart(Long menuId, int amount, HttpSession session) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        String sessionId = session.getId();

        Cart cart = Cart.builder()
                .menu(menu)
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .calorie(menu.getCalorie())
                .amount(amount)
                .sessionId(sessionId)
                .build();

        cartRepository.save(cart);
    }

    // 세션에 해당하는 장바구니의 총 수량 계산
    public int getTotalQuantity(HttpSession session) {
        List<Cart> cartItems = cartRepository.findBySessionId(session.getId());
        return cartItems.stream().mapToInt(Cart::getAmount).sum();  // 모든 아이템의 수량 합산
    }

    // 세션별 장바구니 아이템 조회
    public List<Cart> getCartBySession(HttpSession session) {
        return cartRepository.findBySessionId(session.getId());
    }

    // 장바구니 비우기 (세션 기준)
    public void clearCartBySession(HttpSession session) {
        cartRepository.deleteBySessionId(session.getId());
    }

    @Transactional
    public void updateAmount(Long cartId, int newAmount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.setAmount(newAmount);
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public void deleteSelectedItems(List<Long> cartIds) {
        cartRepository.deleteAllById(cartIds);
    }

    @Transactional
    public void clearCart(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }

    public List<Cart> getCartItems(String sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }
}
