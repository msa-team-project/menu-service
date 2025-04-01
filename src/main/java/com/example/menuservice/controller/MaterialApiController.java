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
    public Iterable<MaterialResponseDTO> getMainMaterials() {
        return materialService.viewMainMaterialList();
    }

    // 메인 재료 조회
    @GetMapping("/{materialName}")
    public MaterialResponseDTO getMainMaterial(@PathVariable String materialName) {
        return materialService.viewMainMaterial(materialName);
    }

    // 메인 재료 추가
    @PostMapping
    public MaterialResponseDTO addMainMaterial(@Valid @RequestBody MaterialRequestDTO materialRequestDTO) {
        return materialService.addMainMaterial(materialRequestDTO);
    }

    // 메인 재료 수정
    @PutMapping("/{materialName}")
    public MaterialResponseDTO updateMainMaterial(@PathVariable String materialName, @Valid @RequestBody MaterialRequestDTO materialRequestDTO) {
        return materialService.editMainMaterialDetails(materialName, materialRequestDTO);
    }

    // 메인 재료 삭제
    @DeleteMapping("/{materialName}")
    public void deleteMainMaterial(@PathVariable String materialName) {
        materialService.removeMainMaterial(materialName);
    }
}


//삭제 안됨