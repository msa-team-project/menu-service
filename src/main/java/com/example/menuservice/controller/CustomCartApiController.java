package com.example.menuservice.controller;

import com.example.menuservice.dto.CustomCartRequestDTO;
import com.example.menuservice.dto.CustomCartResponseDTO;
import com.example.menuservice.service.CustomCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus/custom-carts")
@RequiredArgsConstructor
public class CustomCartApiController {

    private final CustomCartService customCartService;

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<CustomCartResponseDTO>> getAllCustomCarts() {
        List<CustomCartResponseDTO> carts = customCartService.viewCustomCartList();
        return ResponseEntity.ok(carts);
    }

    // 단일 조회
    @GetMapping("/{uid}")
    public ResponseEntity<CustomCartResponseDTO> getCustomCart(@PathVariable Long uid) {
        CustomCartResponseDTO cart = customCartService.viewCustomCart(uid);
        return ResponseEntity.ok(cart);
    }

    // 추가
    @PostMapping
    public ResponseEntity<CustomCartResponseDTO> createCustomCart(@Valid @RequestBody CustomCartRequestDTO dto) {
        CustomCartResponseDTO created = customCartService.addCustomCart(dto);
        return ResponseEntity.ok(created);
    }

    // 삭제
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteCustomCart(@PathVariable Long uid) {
        customCartService.removeCustomCart(uid);
        return ResponseEntity.noContent().build();
    }
}
