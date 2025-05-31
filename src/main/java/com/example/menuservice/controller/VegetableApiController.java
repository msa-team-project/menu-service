package com.example.menuservice.controller;

import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.exception.VegetableAlreadyExistsException;
import com.example.menuservice.exception.VegetableNotFoundException;
import com.example.menuservice.service.VegetableService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/vegetables")
public class VegetableApiController {

    private final VegetableService vegetableService;
    private final FileUploadService fileUploadService;

    // 채소 목록 조회
    @GetMapping
    public ResponseEntity<List<VegetableResponseDTO>> getVegetables() {
        return ResponseEntity.ok(vegetableService.viewVegetableList());
    }

    // 채소 조회
    @GetMapping("/{vegetableName}")
    public ResponseEntity<?> getVegetable(@PathVariable String vegetableName) {
        try {
            return ResponseEntity.ok(vegetableService.viewVegetable(vegetableName));
        } catch (VegetableNotFoundException e) {
            log.warn("채소를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 채소 추가
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addVegetable(
            @Valid @RequestPart("vegetable") String vegetableRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            VegetableResponseDTO response = vegetableService.addVegetable(vegetableRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (VegetableAlreadyExistsException e) {
            log.warn("중복된 채소 이름: {}", e.getMessage());
            return ResponseEntity.status(409).body("이미 존재하는 채소입니다.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("채소 추가 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 채소 수정
    @PutMapping(value = "/{vegetableName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateVegetable(
            @PathVariable String vegetableName,
            @Valid @RequestPart("vegetable") String vegetableRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            VegetableResponseDTO response = vegetableService.editVegetableDetails(vegetableName, vegetableRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (VegetableNotFoundException e) {
            log.warn("수정할 채소를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("채소 수정 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 채소 삭제
    @DeleteMapping("/{vegetableName}")
    public ResponseEntity<?> deleteVegetable(@PathVariable String vegetableName) {
        try {
            vegetableService.removeVegetable(vegetableName);
            return ResponseEntity.noContent().build();
        } catch (VegetableNotFoundException e) {
            log.warn("삭제할 채소를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("채소 삭제 중 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }
}
