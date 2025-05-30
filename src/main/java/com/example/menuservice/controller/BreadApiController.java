package com.example.menuservice.controller;

import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.exception.BreadAlreadyExistsException;
import com.example.menuservice.exception.BreadNotFoundException;
import com.example.menuservice.service.BreadService;
import com.example.menuservice.service.FileUploadService;
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
@RequestMapping("/menus/breads")
public class BreadApiController {

    private final BreadService breadService;
    private final FileUploadService fileUploadService;

    @GetMapping
    public ResponseEntity<List<BreadResponseDTO>> getBreads() {
        return ResponseEntity.ok(breadService.viewBreadList());
    }

    @GetMapping("/{breadName}")
    public ResponseEntity<?> getBread(@PathVariable String breadName) {
        try {
            return ResponseEntity.ok(breadService.viewBread(breadName));
        } catch (BreadNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBread(
            @RequestPart("bread") String breadRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            BreadResponseDTO response = breadService.addBread(breadRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (BreadAlreadyExistsException e) {
            log.warn("중복된 빵 이름: {}", e.getMessage());
            return ResponseEntity.status(409).body("이미 존재하는 빵입니다.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("빵 추가 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    @PutMapping(value = "/{breadName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBread(
            @PathVariable String breadName,
            @RequestPart("bread") String breadRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            BreadResponseDTO response = breadService.editBreadDetails(breadName, breadRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (BreadNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("빵 수정 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    @DeleteMapping("/{breadName}")
    public ResponseEntity<?> deleteBread(@PathVariable String breadName) {
        try {
            breadService.removeBread(breadName);
            return ResponseEntity.noContent().build();
        } catch (BreadNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("빵 삭제 중 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }
}
