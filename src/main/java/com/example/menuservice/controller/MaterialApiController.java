package com.example.menuservice.controller;

import com.example.menuservice.dto.MaterialRequestDTO;
import com.example.menuservice.dto.MaterialResponseDTO;
import com.example.menuservice.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materials")
public class MaterialApiController {

    private final MaterialService materialService;

    // 메인 재료 목록 조회
    @GetMapping
    public Iterable<MaterialResponseDTO> getMaterials() {
        return materialService.viewMainMaterialList();
    }

    // 메인 재료 조회
    @GetMapping("/{materialName}")
    public MaterialResponseDTO getMaterial(@PathVariable String materialName) {
        return materialService.viewMainMaterial(materialName);
    }

    // 메인 재료 추가
    @PostMapping
    public MaterialResponseDTO addMaterial(@Valid @RequestBody MaterialRequestDTO materialRequestDTO) {
        return materialService.addMainMaterial(materialRequestDTO);
    }

    // 메인 재료 수정
    @PutMapping("/{materialName}")
    public MaterialResponseDTO updateMaterial(@PathVariable String materialName, @Valid @RequestBody MaterialRequestDTO materialRequestDTO) {
        return materialService.editMainMaterialDetails(materialName, materialRequestDTO);
    }

    // 메인 재료 삭제
    @DeleteMapping("/{materialName}")
    public void deleteMaterial(@PathVariable String materialName) {
        materialService.removeMainMaterial(materialName);
    }
    //     상태 업데이트
    @PatchMapping("/{uid}/status")
    public void updateMaterialStatus(@PathVariable Long uid, @RequestParam String status) {
        materialService.updateMainMaterialStatus(uid, status);
    }
}


//삭제 안됨