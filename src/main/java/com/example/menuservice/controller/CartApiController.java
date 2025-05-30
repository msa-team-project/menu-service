package com.example.menuservice.controller;

import com.example.menuservice.dto.CartItemsDTO;
import com.example.menuservice.dto.CartResponseDTO;
import com.example.menuservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menus/cart")
@RequiredArgsConstructor
public class CartApiController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCartItems(@RequestParam(required = false) Long userUid,
                                          @RequestParam(required = false) Long socialUid) {
        try {
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            log.error("장바구니 조회 실패", e);
            return ResponseEntity.internalServerError().body("장바구니 조회 실패");
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id,
                                            @RequestParam("amount") int amount,
                                            @RequestParam(required = false) Long userUid,
                                            @RequestParam(required = false) Long socialUid) {
        try {
            cartService.updateAmount(id, amount);
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            log.error("장바구니 수량 변경 실패", e);
            return ResponseEntity.badRequest().body("수량 변경 실패");
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id,
                                            @RequestParam(required = false) Long userUid,
                                            @RequestParam(required = false) Long socialUid) {
        try {
            cartService.deleteItem(id);
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            log.error("장바구니 항목 삭제 실패", e);
            return ResponseEntity.internalServerError().body("항목 삭제 실패");
        }
    }

    @PostMapping("/delete-selected")
    public ResponseEntity<?> deleteSelectedItems(@RequestParam List<Long> selectedIds,
                                                 @RequestParam(required = false) Long userUid,
                                                 @RequestParam(required = false) Long socialUid) {
        try {
            cartService.deleteSelectedItems(selectedIds);
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            log.error("선택 항목 삭제 실패", e);
            return ResponseEntity.internalServerError().body("선택 삭제 실패");
        }
    }

    @PostMapping("/order/checkout")
    public ResponseEntity<?> checkout(@RequestParam(required = false) Long userUid,
                                      @RequestParam(required = false) Long socialUid) {
        try {
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);

            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body("장바구니가 비어 있습니다.");
            }

            cartService.clearCart(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(List.of()));
        } catch (Exception e) {
            log.error("결제 처리 실패", e);
            return ResponseEntity.internalServerError().body("결제 실패");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam("menuId") Long menuId,
                                       @RequestParam("amount") int amount,
                                       @RequestParam(required = false) Long userUid,
                                       @RequestParam(required = false) Long socialUid) {
        try {
            log.debug("장바구니 추가 요청 - userUid: {}, socialUid: {}, menuId: {}, amount: {}",
                    userUid, socialUid, menuId, amount);

            cartService.addToCart(userUid, socialUid, menuId, amount);
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            log.error("장바구니 추가 실패", e);
            return ResponseEntity.badRequest().body("장바구니 추가 실패: " + e.getMessage());
        }
    }

    @PostMapping("/add/side")
    public ResponseEntity<?> addSideToCart(@RequestParam(value = "sideId", required = false) Long sideId,
                                           @RequestParam("amount") int amount,
                                           @RequestParam(required = false) Long userUid,
                                           @RequestParam(required = false) Long socialUid) {
        try {
            cartService.addSideToCart(userUid, socialUid, sideId, amount);
            List<CartItemsDTO> cartItems = cartService.getAllCartItems(userUid, socialUid);
            return ResponseEntity.ok(new CartResponseDTO(cartItems));
        } catch (Exception e) {
            log.error("사이드 추가 실패", e);
            return ResponseEntity.badRequest().body("사이드 추가 실패: " + e.getMessage());
        }
    }
}
