package com.example.menuservice.controller;

import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.exception.SideAlreadyExistsException;
import com.example.menuservice.exception.SideNotFoundException;
import com.example.menuservice.service.SideService;
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
@RequestMapping("/menus/sides")
public class SideApiController {

    private final SideService sideService;
    private final FileUploadService fileUploadService;

    // 사이드 목록 조회
    @GetMapping
    public ResponseEntity<List<SideResponseDTO>> getSides() {
        return ResponseEntity.ok(sideService.viewSideList());
    }

    // 사이드 조회
    @GetMapping("/{sideName}")
    public ResponseEntity<?> getSide(@PathVariable String sideName) {
        try {
            return ResponseEntity.ok(sideService.viewSide(sideName));
        } catch (SideNotFoundException e) {
            log.warn("사이드를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 사이드 추가
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addSide(
            @Valid @RequestPart("side") String sideRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            SideResponseDTO response = sideService.addSide(sideRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (SideAlreadyExistsException e) {
            log.warn("중복된 사이드 이름: {}", e.getMessage());
            return ResponseEntity.status(409).body("이미 존재하는 사이드입니다.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("사이드 추가 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 사이드 수정
    @PutMapping(value = "/{sideName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateSide(
            @PathVariable String sideName,
            @Valid @RequestPart("side") String sideRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            SideResponseDTO response = sideService.editSideDetails(sideName, sideRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (SideNotFoundException e) {
            log.warn("수정할 사이드를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("사이드 수정 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 사이드 삭제
    @DeleteMapping("/{sideName}")
    public ResponseEntity<?> deleteSide(@PathVariable String sideName) {
        try {
            sideService.removeSide(sideName);
            return ResponseEntity.noContent().build();
        } catch (SideNotFoundException e) {
            log.warn("삭제할 사이드를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("사이드 삭제 중 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }
}
