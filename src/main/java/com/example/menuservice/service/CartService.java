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

    private CartItemsDTO toCartItemDTO(Cart cart) {
        Long unitPrice = 0L;
        String imageUrl = null;

        if (cart.getMenu() != null) {
            unitPrice = cart.getMenu().getPrice();
            imageUrl = cart.getMenu().getImg();
        } else if (cart.getCustomCart() != null) {
            unitPrice = cart.getCustomCart().getPrice();
        } else if (cart.getSide() != null) {
            unitPrice = (long) cart.getSide().getPrice();
            imageUrl = cart.getSide().getImg();
        }

        return CartItemsDTO.builder()
                .uid(cart.getUid())
                .userUid(cart.getUserUid())
                .socialUid(cart.getSocialUid())
                .menuName(cart.getMenuName())
                .amount(cart.getAmount())
                .totalPrice(cart.getPrice())
                .calorie(cart.getCalorie())
                .unitPrice(unitPrice)
                .img(imageUrl)
                .build();
    }

    public List<CartItemsDTO> getAllCartItems(Long userUid, Long socialUid) {
        List<Cart> carts = (userUid != null) ?
                cartRepository.findByUserUid(userUid) :
                cartRepository.findBySocialUid(socialUid);

        return carts.stream().map(this::toCartItemDTO).collect(Collectors.toList());
    }

    @Transactional
    public void addToCart(Long userUid, Long socialUid, Long menuId, int amount) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));

        Long totalPrice = menu.getPrice() * amount;
        Double totalCalorie = menu.getCalorie() * amount;

        Optional<Cart> existingCart = (userUid != null) ?
                cartRepository.findByMenuAndUserUid(menu, userUid) :
                cartRepository.findByMenuAndSocialUid(menu, socialUid);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setAmount(cart.getAmount() + amount);
            cart.setPrice(cart.getPrice() + totalPrice);
            cart.setCalorie(cart.getCalorie() + totalCalorie);
        } else {
            Cart cart = Cart.builder()
                    .menu(menu)
                    .menuName(menu.getMenuName())
                    .price(totalPrice)
                    .calorie(totalCalorie)
                    .amount(amount)
                    .userUid(userUid)
                    .socialUid(socialUid)
                    .build();
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void addSideToCart(Long userUid, Long socialUid, Long sideId, int amount) {
        Side side = sideRepository.findById(sideId)
                .orElseThrow(() -> new RuntimeException("해당 사이드를 찾을 수 없습니다."));

        long totalPrice = (long) side.getPrice() * amount;
        Double totalCalorie = side.getCalorie() * amount;

        Optional<Cart> existing = (userUid != null) ?
                cartRepository.findBySideAndUserUid(side, userUid) :
                cartRepository.findBySideAndSocialUid(side, socialUid);

        if (existing.isPresent()) {
            Cart cart = existing.get();
            cart.setAmount(cart.getAmount() + amount);
            cart.setPrice(cart.getPrice() + totalPrice);
            cart.setCalorie(cart.getCalorie() + totalCalorie);
        } else {
            Cart cart = Cart.builder()
                    .side(side)
                    .menuName(side.getSideName())
                    .price(totalPrice)
                    .calorie(totalCalorie)
                    .amount(amount)
                    .userUid(userUid)
                    .socialUid(socialUid)
                    .build();
            cartRepository.save(cart);
        }
    }

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
            cart.setPrice((long) cart.getSide().getPrice() * newAmount);
            cart.setCalorie(cart.getSide().getCalorie() * newAmount);
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
    public void clearCart(Long userUid, Long socialUid) {
        List<Cart> items = (userUid != null) ?
                cartRepository.findByUserUid(userUid) :
                cartRepository.findBySocialUid(socialUid);
        cartRepository.deleteAll(items);
    }
}
