package com.example.menuservice.dto;

import com.example.menuservice.domain.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    private int totalQuantity; // 총 수량
    private long totalPrice;   // 총 가격
    private List<Cart> cartItems; // 장바구니 아이템 리스트

    // CartResponseDTO 생성자에서, 장바구니 항목 리스트와 총 수량 및 총 가격 계산
    public CartResponseDTO(List<Cart> cartItems) {
        this.cartItems = cartItems;
        this.totalQuantity = cartItems.stream().mapToInt(Cart::getAmount).sum();
        this.totalPrice = cartItems.stream().mapToLong(item -> item.getAmount() * item.getPrice()).sum();
    }
}
