package com.example.menuservice.controller;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.dto.CartResponseDTO;

import com.example.menuservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    // 장바구니 전체 조회 API
    @GetMapping
    public ResponseEntity<?> getCartItems() {
        List<Cart> cartItems = cartService.getAllCartItems();

        int totalQuantity = cartItems.stream().mapToInt(Cart::getAmount).sum();
        long totalPrice = cartItems.stream().mapToLong(item -> item.getAmount() * item.getPrice()).sum();

        return ResponseEntity.ok(new CartResponseDTO(totalQuantity, totalPrice, cartItems));
    }

    // 장바구니 항목 수량 변경
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestParam("amount") int amount) {
        cartService.updateAmount(id, amount);
        return ResponseEntity.ok("수량이 업데이트되었습니다.");
    }



    // 장바구니 항목 단건 삭제
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return ResponseEntity.ok("항목이 삭제되었습니다.");
    }

    // 선택 항목들 삭제
    @PostMapping("/delete-selected")
    public ResponseEntity<?> deleteSelectedItems(@RequestParam List<Long> selectedIds) {
        cartService.deleteSelectedItems(selectedIds);
        return ResponseEntity.ok("선택한 항목들이 삭제되었습니다.");
    }

    // 결제 처리
    @PostMapping("/order/checkout")
    public ResponseEntity<?> checkout() {
        List<Cart> cartItems = cartService.getAllCartItems();

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        }

        cartService.clearCart();
        return ResponseEntity.ok("결제가 완료되었습니다.");
    }
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam("menuId") Long menuId,

                                       @RequestParam("amount") int amount) {
        cartService.addToCart(menuId,  amount);
        return ResponseEntity.ok("장바구니에 담겼습니다.");
    }


}
