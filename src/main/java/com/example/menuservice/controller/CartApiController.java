package com.example.menuservice.controller;

import com.example.menuservice.domain.Cart;
import com.example.menuservice.service.CartService;
import com.example.menuservice.dto.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    // 장바구니 목록 조회 API
    @GetMapping
    public ResponseEntity<?> getCartItems(HttpSession session) {
        String sessionId = session.getId();
        List<Cart> cartItems = cartService.getCartItems(sessionId);

        // 총 수량과 총 가격 계산
        int totalQuantity = cartItems.stream().mapToInt(Cart::getAmount).sum();
        long totalPrice = cartItems.stream().mapToLong(item -> item.getAmount() * item.getPrice()).sum();

        // 장바구니 정보를 DTO로 반환
        return ResponseEntity.ok(new CartResponseDTO(totalQuantity, totalPrice, cartItems));
    }



    // 장바구니 항목 수량 변경 API
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestParam("amount") int amount) {
        cartService.updateAmount(id, amount);
        return ResponseEntity.ok("수량이 업데이트되었습니다.");
    }

    // 장바구니 항목 삭제 API
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return ResponseEntity.ok("항목이 삭제되었습니다.");
    }

    // 선택한 항목들 삭제 API
    @PostMapping("/delete-selected")
    public ResponseEntity<?> deleteSelectedItems(@RequestParam List<Long> selectedIds) {
        cartService.deleteSelectedItems(selectedIds);
        return ResponseEntity.ok("선택한 항목들이 삭제되었습니다.");
    }

    // 결제 API
    @PostMapping("/order/checkout")
    public ResponseEntity<?> checkout(HttpSession session) {
        String sessionId = session.getId();
        List<Cart> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        }

        cartService.clearCart(sessionId);
        return ResponseEntity.ok("결제가 완료되었습니다.");
    }
}
