package com.example.menuservice.controller;

import com.example.menuservice.dto.CartItemsDTO;
import com.example.menuservice.dto.CartResponseDTO;
import com.example.menuservice.dto.SideCartRequestDTO;
import com.example.menuservice.dto.SideRequestDTO;
import com.example.menuservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    // 장바구니 전체 조회
    @GetMapping
    public ResponseEntity<?> getCartItems() {
        List<CartItemsDTO> cartItems = cartService.getAllCartItems();
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // 장바구니 항목 수량 변경
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestParam("amount") int amount) {
        cartService.updateAmount(id, amount);
        List<CartItemsDTO> cartItems = cartService.getAllCartItems();
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // 장바구니 항목 단건 삭제
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        List<CartItemsDTO> cartItems = cartService.getAllCartItems();
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // 선택 항목들 삭제
    @PostMapping("/delete-selected")
    public ResponseEntity<?> deleteSelectedItems(@RequestParam List<Long> selectedIds) {
        cartService.deleteSelectedItems(selectedIds);
        List<CartItemsDTO> cartItems = cartService.getAllCartItems();
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // 결제 처리
    @PostMapping("/order/checkout")
    public ResponseEntity<?> checkout() {
        List<CartItemsDTO> cartItems = cartService.getAllCartItems();

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        }

        cartService.clearCart();
        return ResponseEntity.ok(new CartResponseDTO(List.of())); // 비운 후 빈 배열 반환
    }

    // 장바구니에 항목 추가
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam("menuId") Long menuId,
                                       @RequestParam("amount") int amount) {
        try {
            cartService.addToCart(menuId, amount);
            List<CartItemsDTO> cartItems = cartService.getAllCartItems();
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("장바구니 추가 실패: " + e.getMessage());
        }
    }
    @PostMapping("/add/side")
    public ResponseEntity<?> addSideToCart(@RequestBody SideCartRequestDTO dto) {
        cartService.addSideToCart(dto);
        return ResponseEntity.ok(cartService.getAllCartItems());
    }

//    @GetMapping("/quantity")
//    public ResponseEntity<Integer> getCartQuantity() {
//        int quantity = cartService.getTotalQuantity(); // 장바구니에 담긴 총 수량을 계산하는 서비스 호출
//        return ResponseEntity.ok(quantity); // 수량 반환
//    }
}

