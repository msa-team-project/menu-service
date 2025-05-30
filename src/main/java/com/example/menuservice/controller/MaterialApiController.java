package com.example.menuservice.controller;

import com.example.menuservice.dto.MaterialResponseDTO;
import com.example.menuservice.exception.MaterialAlreadyExistsException;
import com.example.menuservice.exception.MaterialNotFoundException;
import com.example.menuservice.service.MaterialService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/materials")
public class MaterialApiController {

    private final MaterialService materialService;
    private final FileUploadService fileUploadService;

    // 메인 재료 목록 조회
    @GetMapping
    public ResponseEntity<Iterable<MaterialResponseDTO>> getMaterials() {
        return ResponseEntity.ok(materialService.viewMaterialList());
    }

    // 메인 재료 조회
    @GetMapping("/{materialName}")
    public ResponseEntity<?> getMaterial(@PathVariable String materialName) {
        try {
            MaterialResponseDTO response = materialService.viewMaterial(materialName);
            return ResponseEntity.ok(response);
        } catch (MaterialNotFoundException e) {
            log.warn("재료를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 메인 재료 추가
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> addMaterial(
            @Valid @RequestPart("material") String materialRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            MaterialResponseDTO response = materialService.addMaterial(materialRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (MaterialAlreadyExistsException e) {
            log.warn("중복된 재료 이름: {}", e.getMessage());
            return ResponseEntity.status(409).body("이미 존재하는 재료입니다.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("재료 추가 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 메인 재료 수정
    @PutMapping(value = "/{materialName}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateMaterial(
            @PathVariable String materialName,
            @Valid @RequestPart("material") String materialRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            MaterialResponseDTO response = materialService.editMaterialDetails(materialName, materialRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (MaterialNotFoundException e) {
            log.warn("수정할 재료를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("재료 수정 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 메인 재료 삭제
    @DeleteMapping("/{materialName}")
    public ResponseEntity<?> deleteMaterial(@PathVariable String materialName) {
        try {
            materialService.removeMaterial(materialName);
            return ResponseEntity.noContent().build();
        } catch (MaterialNotFoundException e) {
            log.warn("삭제할 재료를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("재료 삭제 중 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }
}
