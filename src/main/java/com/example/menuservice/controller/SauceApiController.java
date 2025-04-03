package com.example.menuservice.controller;

import com.example.menuservice.dto.SauceRequestDTO;
import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.service.SauceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sauces")
public class SauceApiController {

    private final SauceService sauceService;

    // 소스 목록 조회
    @GetMapping
    public Iterable<SauceResponseDTO> getSauces() {
        return sauceService.viewSauceList();
    }

    // 소스 이름으로 소스 조회
    @GetMapping("/{sauceName}")
    public SauceResponseDTO getSauce(@PathVariable String sauceName) {
        return sauceService.viewSauce(sauceName);
    }

    // 소스 추가
    @PostMapping
    public SauceResponseDTO addSauce(@Valid @RequestBody SauceRequestDTO sauceRequestDTO) {
        return sauceService.addSauce(sauceRequestDTO);
    }

    // 소스 수정
    @PutMapping("/{sauceName}")
    public SauceResponseDTO updateSauce(@PathVariable String sauceName, @Valid @RequestBody SauceRequestDTO sauceRequestDTO) {
        return sauceService.editSauceDetails(sauceName, sauceRequestDTO);
    }

    // 소스 삭제
    @DeleteMapping("/{sauceName}")
    public void deleteSauce(@PathVariable String sauceName) {
        sauceService.removeSauce(sauceName);
    }

    //     상태 업데이트
    @PatchMapping("/{uid}/status")
    public void updateSauceStatus(@PathVariable Long uid, @RequestParam String status) {
        sauceService.updateSauceStatus(uid, status);
    }
}
