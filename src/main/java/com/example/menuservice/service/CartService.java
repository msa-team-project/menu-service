package com.example.menuservice.service;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.domain.Menu;
import com.example.menuservice.domain.Side;
import com.example.menuservice.dto.CartItemsDTO;
import com.example.menuservice.dto.SideCartRequestDTO;
import com.example.menuservice.exception.CartItemNotFoundException;
import com.example.menuservice.repository.CartRepository;
import com.example.menuservice.repository.MenuRepository;
import com.example.menuservice.repository.SideRepository;
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
    private final SideRepository sideRepository;

    // DTO 변환
    private CartItemsDTO toCartItemDTO(Cart cart) {
        Long unitPrice;
        if (cart.getMenu() != null) {
            unitPrice = cart.getMenu().getPrice();
        } else if (cart.getCustomCart() != null) {
            unitPrice = cart.getCustomCart().getPrice();
        } else if (cart.getSide() != null) {
            unitPrice = (long) cart.getSide().getPrice();
        } else {
            unitPrice = 0L;
        }

        return new CartItemsDTO(
                cart.getUid(),
                cart.getMenuName(),
                cart.getAmount(),
                cart.getPrice(),
                cart.getCalorie(),
                unitPrice

        );
    }

    // 전체 장바구니 조회
    public List<CartItemsDTO> getAllCartItems() {
        return cartRepository.findAll().stream()
                .map(this::toCartItemDTO)
                .collect(Collectors.toList());
    }

    // 메뉴 담기
    @Transactional
    public void addToCart(Long menuId, int amount) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

        Long totalPrice = menu.getPrice() * amount;
        Double totalCalorie = menu.getCalorie() * amount;

        Optional<Cart> existingCart = cartRepository.findByMenu(menu);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setAmount(cart.getAmount() + amount);
            cart.setPrice(cart.getPrice() + totalPrice);
            cart.setCalorie(cart.getCalorie() + totalCalorie);
            cartRepository.save(cart);
        } else {
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

    // 사이드 담기
    @Transactional
    public void addSideToCart(SideCartRequestDTO dto) {
        Side side = sideRepository.findById(dto.getUid())
                .orElseThrow(() -> new RuntimeException("해당 사이드를 찾을 수 없습니다."));

        int totalPrice = side.getPrice() * dto.getAmount();
        Double totalCalorie = side.getCalorie() * dto.getAmount();

        Optional<Cart> existing = cartRepository.findBySide(side);

        if (existing.isPresent()) {
            Cart cart = existing.get();
            cart.setAmount(cart.getAmount() + dto.getAmount());
            cart.setPrice(cart.getPrice() + totalPrice);
            cart.setCalorie(cart.getCalorie() + totalCalorie);
            cartRepository.save(cart);
        } else {
            Cart cart = Cart.builder()
                    .side(side)
                    .menuName(side.getSideName())
                    .price((long) totalPrice)
                    .calorie(totalCalorie)
                    .amount(dto.getAmount())
                    .build();
            cartRepository.save(cart);
        }
    }


    // 수량 변경
    @Transactional
    public void updateAmount(Long cartId, int newAmount) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartItemNotFoundException("장바구니 항목을 찾을 수 없습니다."));

        if (newAmount < 1) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        cart.setAmount(newAmount);

        if (cart.getMenu() != null) {
            cart.setPrice(cart.getMenu().getPrice() * newAmount);
            cart.setCalorie(cart.getMenu().getCalorie() * newAmount);
        } else if (cart.getCustomCart() != null) {
            cart.setPrice(cart.getCustomCart().getPrice() * newAmount);
            cart.setCalorie(cart.getCustomCart().getCalorie() * newAmount);
        } else if (cart.getSide() != null) {
            cart.setPrice(((long) cart.getSide().getPrice() * newAmount));
            cart.setCalorie(cart.getSide().getCalorie() * newAmount);
        } else {
            throw new IllegalStateException("장바구니 항목에는 Menu, CustomCart 또는 Side가 있어야 합니다.");
        }

        cartRepository.save(cart);
    }

    // 단일 항목 삭제
    @Transactional
    public void deleteItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    // 여러 항목 삭제
    @Transactional
    public void deleteSelectedItems(List<Long> cartIds) {
        cartRepository.deleteAllById(cartIds);
    }

    // 전체 장바구니 비우기
    @Transactional
    public void clearCart() {
        cartRepository.deleteAll();
    }
}
