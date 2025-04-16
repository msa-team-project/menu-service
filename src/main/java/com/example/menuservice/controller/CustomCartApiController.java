package com.example.menuservice.controller;

import com.example.menuservice.dto.CustomCartRequestDTO;
import com.example.menuservice.dto.CustomCartResponseDTO;
import com.example.menuservice.service.CustomCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customCart")
public class CustomCartApiController {

    private final CustomCartService customCartService;

    /**
     * 커스텀 카트 추가
     */
    @PostMapping
    public ResponseEntity<CustomCartResponseDTO> addCustomCart(@RequestBody CustomCartRequestDTO customCartRequestDTO) {
        // 기본값으로 "guest" 처리 (세션 없이 바로 처리)
        String sessionId = "guest";

        // 커스텀 카트 저장
        CustomCartResponseDTO customCartResponse = customCartService.addCustomCart(customCartRequestDTO, sessionId);

        return ResponseEntity.ok(customCartResponse);
    }
}
