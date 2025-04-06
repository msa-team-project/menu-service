package com.example.menuservice.controller;

import com.example.menuservice.dto.MaterialRequestDTO;
import com.example.menuservice.dto.MaterialResponseDTO;
import com.example.menuservice.service.MaterialService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/materials")
public class MaterialApiController {

    private final MaterialService materialService;
    private final FileUploadService fileUploadService;

    // 메인 재료 목록 조회
    @GetMapping
    public Iterable<MaterialResponseDTO> getMaterials() {
        return materialService.viewMaterialList();
    }

    // 메인 재료 조회
    @GetMapping("/{materialName}")
    public MaterialResponseDTO getMaterial(@PathVariable String materialName) {
        return materialService.viewMaterial(materialName);
    }

    // 메인 재료 추가
    @PostMapping
    public ResponseEntity<MaterialResponseDTO> addMaterial(
            @Valid @RequestPart("material") MaterialRequestDTO materialRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            if (file != null && !file.isEmpty()) {
                String fileUrl = fileUploadService.uploadFile(file);
                materialRequestDTO.setImg(fileUrl); // DTO에 이미지 URL 저장
            }

            MaterialResponseDTO response = materialService.addMaterial(materialRequestDTO,file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 메인 재료 수정
    @PutMapping("/{materialName}")
    public ResponseEntity<MaterialResponseDTO> updateMaterial(
            @PathVariable String materialName,
            @Valid @RequestPart("material") MaterialRequestDTO materialRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            if (file != null && !file.isEmpty()) {
                String fileUrl = fileUploadService.uploadFile(file);
                materialRequestDTO.setImg(fileUrl); // DTO에 이미지 URL 저장
            }

            MaterialResponseDTO response = materialService.editMaterialDetails(materialName, materialRequestDTO,file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 메인 재료 삭제
    @DeleteMapping("/{materialName}")
    public void deleteMaterial(@PathVariable String materialName) {
        materialService.removeMaterial(materialName);
    }
}
