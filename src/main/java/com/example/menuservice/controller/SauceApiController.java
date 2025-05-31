package com.example.menuservice.controller;

import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.exception.SauceAlreadyExistsException;
import com.example.menuservice.exception.SauceNotFoundException;
import com.example.menuservice.service.SauceService;
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
@RequestMapping("/menus/sauces")
public class SauceApiController {

    private final SauceService sauceService;
    private final FileUploadService fileUploadService;

    // 소스 목록 조회
    @GetMapping
    public ResponseEntity<List<SauceResponseDTO>> getSauces() {
        return ResponseEntity.ok(sauceService.viewSauceList());
    }

    // 소스 조회
    @GetMapping("/{sauceName}")
    public ResponseEntity<?> getSauce(@PathVariable String sauceName) {
        try {
            return ResponseEntity.ok(sauceService.viewSauce(sauceName));
        } catch (SauceNotFoundException e) {
            log.warn("소스를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 소스 추가
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addSauce(
            @Valid @RequestPart("sauce") String sauceRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            SauceResponseDTO response = sauceService.addSauce(sauceRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (SauceAlreadyExistsException e) {
            log.warn("중복된 소스 이름: {}", e.getMessage());
            return ResponseEntity.status(409).body("이미 존재하는 소스입니다.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("소스 추가 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 소스 수정
    @PutMapping(value = "/{sauceName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateSauce(
            @PathVariable String sauceName,
            @Valid @RequestPart("sauce") String sauceRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            SauceResponseDTO response = sauceService.editSauceDetails(sauceName, sauceRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (SauceNotFoundException e) {
            log.warn("수정할 소스를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("소스 수정 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 소스 삭제
    @DeleteMapping("/{sauceName}")
    public ResponseEntity<?> deleteSauce(@PathVariable String sauceName) {
        try {
            sauceService.removeSauce(sauceName);
            return ResponseEntity.noContent().build();
        } catch (SauceNotFoundException e) {
            log.warn("삭제할 소스를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("소스 삭제 중 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }
}
