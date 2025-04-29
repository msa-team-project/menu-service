package com.example.menuservice.controller;

import com.example.menuservice.dto.VegetableRequestDTO;
import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.service.VegetableService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/vegetables")
public class VegetableApiController {

    private final VegetableService vegetableService;
    private final FileUploadService fileUploadService;

    // 채소 목록 조회
    @GetMapping
    public Iterable<VegetableResponseDTO> getVegetables() {
        return vegetableService.viewVegetableList();
    }

    // 채소 조회
    @GetMapping("/{vegetableName}")
    public VegetableResponseDTO getVegetable(@PathVariable String vegetableName) {
        return vegetableService.viewVegetable(vegetableName);
    }

    // 채소 추가
    @PostMapping
    public ResponseEntity<VegetableResponseDTO> addVegetable(
            @Valid @RequestPart("vegetable") String vegetableRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            VegetableResponseDTO response = vegetableService.addVegetable(vegetableRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 채소 수정
    @PutMapping("/{vegetableName}")
    public ResponseEntity<VegetableResponseDTO> updateVegetable(
            @PathVariable String vegetableName,
            @Valid @RequestPart("vegetable") String vegetableRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            VegetableResponseDTO response = vegetableService.editVegetableDetails(vegetableName, vegetableRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 채소 삭제
    @DeleteMapping("/{vegetableName}")
    public void deleteVegetable(@PathVariable String vegetableName) {
        vegetableService.removeVegetable(vegetableName);
    }
}
