package com.example.menuservice.controller;

import com.example.menuservice.dto.SauceRequestDTO;
import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.service.SauceService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/sauces")
public class SauceApiController {

    private final SauceService sauceService;
    private final FileUploadService fileUploadService;

    // 소스 목록 조회
    @GetMapping
    public Iterable<SauceResponseDTO> getSauces() {
        return sauceService.viewSauceList();
    }

    // 소스 조회
    @GetMapping("/{sauceName}")
    public SauceResponseDTO getSauce(@PathVariable String sauceName) {
        return sauceService.viewSauce(sauceName);
    }

    // 소스 추가
    @PostMapping
    public ResponseEntity<SauceResponseDTO> addSauce(
            @Valid @RequestPart("sauce") String sauceRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 업로드는 Service에서 처리
            SauceResponseDTO response = sauceService.addSauce(sauceRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 소스 수정
    @PutMapping("/{sauceName}")
    public ResponseEntity<SauceResponseDTO> updateSauce(
            @PathVariable String sauceName,
            @Valid @RequestPart("sauce") String sauceRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 업로드는 Service에서 처리
            SauceResponseDTO response = sauceService.editSauceDetails(sauceName, sauceRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 소스 삭제
    @DeleteMapping("/{sauceName}")
    public void deleteSauce(@PathVariable String sauceName) {
        sauceService.removeSauce(sauceName);
    }
}
