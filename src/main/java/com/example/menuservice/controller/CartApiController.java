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

    // 🛒 장바구니 전체 조회
    @GetMapping
    public ResponseEntity<?> getCartItems(@RequestParam(required = false) Long userUid,
                                          @RequestParam(required = false) Long socialUid) {
        List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // 🧾 수량 변경
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id,
                                            @RequestParam("amount") int amount,
                                            @RequestParam(required = false) Long userUid,
                                            @RequestParam(required = false) Long socialUid) {
        cartService.updateAmount(id, amount);
        List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // ❌ 단일 항목 삭제
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id,
                                            @RequestParam(required = false) Long userUid,
                                            @RequestParam(required = false) Long socialUid) {
        cartService.deleteItem(id);
        List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // ❌ 선택 항목 삭제
    @PostMapping("/delete-selected")
    public ResponseEntity<?> deleteSelectedItems(@RequestParam List<Long> selectedIds,
                                                 @RequestParam(required = false) Long userUid,
                                                 @RequestParam(required = false) Long socialUid) {
        cartService.deleteSelectedItems(selectedIds);
        List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }

    // ✅ 결제 완료 시 장바구니 전체 삭제
    @PostMapping("/order/checkout")
    public ResponseEntity<?> checkout(@RequestParam(required = false) Long userUid,
                                      @RequestParam(required = false) Long socialUid) {
        List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
        }

        cartService.clearCart(userUid, socialUid);
        return ResponseEntity.ok(new CartResponseDTO(List.of()));
    }

    // ➕ 메뉴 추가
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam("menuId") Long menuId,
                                       @RequestParam("amount") int amount,
                                       @RequestParam(required = false) Long userUid,
                                       @RequestParam(required = false) Long socialUid) {
        try {
            System.out.println("userUid = " + userUid); // 출력 확인용
            System.out.println("socialUid = " + socialUid);
            cartService.addToCart(userUid, socialUid, menuId, amount);
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("장바구니 추가 실패: " + e.getMessage());
        }
    }

    // ➕ 사이드 추가
    @PostMapping("/add/side")
    public ResponseEntity<?> addSideToCart(@RequestParam(value = "sideId", required = false) Long sideId,
                                           @RequestParam("amount") int amount,
                                           @RequestParam(required = false) Long userUid,
                                           @RequestParam(required = false) Long socialUid) {
        cartService.addSideToCart(userUid, socialUid, sideId, amount);
        List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
        return ResponseEntity.ok(new CartResponseDTO(cartItems));
    }
}
