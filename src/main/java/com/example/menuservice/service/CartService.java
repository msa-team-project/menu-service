package com.example.menuservice.service;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.domain.Menu;
import com.example.menuservice.dto.CartItemsDTO;
import com.example.menuservice.exception.CartItemNotFoundException;
import com.example.menuservice.repository.CartRepository;
import com.example.menuservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    // Cart -> CartItemDTO 변환 메서드
    private CartItemsDTO toCartItemDTO(Cart cart) {
        Long unitPrice = cart.getAmount() == 0 ? 0 : cart.getPrice() / cart.getAmount();

        return new CartItemsDTO(
                cart.getUid(),
                cart.getMenuName(),
                cart.getAmount(),
                cart.getPrice(),
                cart.getCalorie(),
                unitPrice
        );
    }


    // 장바구니 전체 조회 (DTO 변환 후 반환)
    public List<CartItemsDTO> getAllCartItems() {
        return cartRepository.findAll().stream()
                .map(this::toCartItemDTO)
                .collect(Collectors.toList());
    }

    // 메뉴 추가 및 장바구니에 담기
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
            cart.setAmount(cart.getAmount() + amount);
            cart.setPrice(cart.getPrice() + totalPrice);  // 가격 재계산
            cart.setCalorie(cart.getCalorie() + totalCalorie);  // 칼로리 재계산
            cartRepository.save(cart);
        } else {
            // 새로 장바구니에 추가
            Cart cart = Cart.builder()
                    .menu(menu)
                    .menuName(menu.getMenuName())
                    .price(totalPrice)
                    .calorie(totalCalorie)
                    .amount(amount)
                    .build();
            cartRepository.save(cart);
        }
    }

    // 수량 업데이트
    @Transactional
    public void updateAmount(Long cartId, int newAmount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartItemNotFoundException("장바구니 항목을 찾을 수 없습니다."));

        if (newAmount < 1) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        cart.setAmount(newAmount);

        if (cart.getMenu() != null) {
            // 메뉴일 경우 가격과 칼로리 재계산
            Long unitPrice = cart.getMenu().getPrice();
            Double unitCalorie = cart.getMenu().getCalorie();
            cart.setPrice(unitPrice * newAmount);
            cart.setCalorie(unitCalorie * newAmount);
        } else if (cart.getCustomCart() != null) {
            // 커스텀 메뉴일 경우 가격과 칼로리 재계산
            Long unitPrice = cart.getCustomCart().getPrice();
            Double unitCalorie = cart.getCustomCart().getCalorie();
            cart.setPrice(unitPrice * newAmount);
            cart.setCalorie(unitCalorie * newAmount);
        } else {
            throw new IllegalStateException("장바구니 항목에는 Menu 또는 CustomCart가 있어야 합니다.");
        }

        cartRepository.save(cart);
    }

    // 장바구니 항목 삭제
    @Transactional
    public void deleteItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // 선택된 장바구니 항목들 삭제
    @Transactional
    public void deleteSelectedItems(List<Long> cartIds) {
        cartRepository.deleteAllById(cartIds);
    }

    // 전체 장바구니 삭제
    @Transactional
    public void clearCart() {
        cartRepository.deleteAll();
    }
}
