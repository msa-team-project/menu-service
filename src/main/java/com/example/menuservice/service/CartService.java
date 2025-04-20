package com.example.menuservice.service;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.domain.Menu;
import com.example.menuservice.repository.CartRepository;
import com.example.menuservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void addToCart(Long menuId, int amount) {
        // 메뉴 정보 조회
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

        // 메뉴의 단가로 가격과 칼로리 계산
        Long totalPrice = menu.getPrice() * amount;
        Double totalCalorie = menu.getCalorie() * amount;

        // 동일한 메뉴가 장바구니에 이미 존재하는지 확인
        Optional<Cart> existingCart = cartRepository.findByMenu(menu);

        if (existingCart.isPresent()) {
            // 기존 장바구니에 있는 메뉴의 수량만 증가
            Cart cart = existingCart.get();
            cart.setAmount(cart.getAmount() + amount); // 수량 증가
            cart.setPrice(cart.getPrice()+menu.getPrice()); // 가격 재계산
            cart.setCalorie(menu.getCalorie()); // 칼로리 재계산
            cartRepository.save(cart); // 수량, 가격, 칼로리 업데이트하여 저장
        } else {
            // 새로 장바구니에 추가
            Cart cart = Cart.builder()
                    .menu(menu)
                    .menuName(menu.getMenuName())
                    .price(totalPrice) // 수량에 맞는 총 가격
                    .calorie(totalCalorie) // 수량에 맞는 총 칼로리
                    .amount(amount) // 수량
                    .build();
            cartRepository.save(cart); // 새로운 항목을 장바구니에 저장
        }
    }



    // 장바구니 전체 조회
    public List<Cart> getAllCartItems() {
        return cartRepository.findAll();
    }

    @Transactional
    public void updateAmount(Long cartId, int newAmount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (newAmount < 1) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        cart.setAmount(newAmount);

        if (cart.getMenu() != null) {
            // 일반 메뉴일 경우
            Long unitPrice = cart.getMenu().getPrice();
            Double unitCalorie = cart.getMenu().getCalorie();

            cart.setPrice(unitPrice * newAmount);
            cart.setCalorie(unitCalorie * newAmount);

        } else if (cart.getCustomCart() != null) {
            // 커스텀 메뉴일 경우
            Long unitPrice = cart.getCustomCart().getPrice();
            Double unitCalorie = cart.getCustomCart().getCalorie();

            cart.setPrice(unitPrice * newAmount);
            cart.setCalorie(unitCalorie * newAmount);

        } else {
            throw new IllegalStateException("Cart must have either a Menu or a CustomCart");
        }

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
    public void clearCart() {
        cartRepository.deleteAll();
    }
}
