package com.example.menuservice.controller;

import com.example.menuservice.dto.MainMaterialRequestDTO;
import com.example.menuservice.dto.MainMaterialResponseDTO;
import com.example.menuservice.service.MainMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main-materials")
public class MainMaterialApiController {

    private final MainMaterialService mainMaterialService;

    // 메인 재료 목록 조회
    @GetMapping
    public Iterable<MainMaterialResponseDTO> getMainMaterials() {
        return mainMaterialService.viewMainMaterialList();
    }

    // 메인 재료 조회
    @GetMapping("/{materialName}")
    public MainMaterialResponseDTO getMainMaterial(@PathVariable String materialName) {
        return mainMaterialService.viewMainMaterial(materialName);
    }

    // 메인 재료 추가
    @PostMapping
    public MainMaterialResponseDTO addMainMaterial(@Valid @RequestBody MainMaterialRequestDTO mainMaterialRequestDTO) {
        return mainMaterialService.addMainMaterial(mainMaterialRequestDTO);
    }

    // 메인 재료 수정
    @PutMapping("/{materialName}")
    public MainMaterialResponseDTO updateMainMaterial(@PathVariable String materialName, @Valid @RequestBody MainMaterialRequestDTO mainMaterialRequestDTO) {
        return mainMaterialService.editMainMaterialDetails(materialName, mainMaterialRequestDTO);
    }

    // 메인 재료 삭제
    @DeleteMapping("/{materialName}")
    public void deleteMainMaterial(@PathVariable String materialName) {
        mainMaterialService.removeMainMaterial(materialName);
    }
}


//삭제 안됨