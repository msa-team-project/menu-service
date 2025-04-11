package com.example.menuservice.controller;

import com.example.menuservice.dto.SideRequestDTO;
import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.service.SideService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sides")
public class SideApiController {

    private final SideService sideService;
    private final FileUploadService fileUploadService;

    // 사이드 목록 조회
    @GetMapping
    public Iterable<SideResponseDTO> getSides() {
        return sideService.viewSideList();
    }

    // 사이드 조회
    @GetMapping("/{sideName}")
    public SideResponseDTO getSide(@PathVariable String sideName) {
        return sideService.viewSide(sideName);
    }

    // 사이드 추가
    @PostMapping
    public ResponseEntity<SideResponseDTO> addSide(
            @Valid @RequestPart("side") SideRequestDTO sideRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 업로드는 Service에서 처리
            SideResponseDTO response = sideService.addSide(sideRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 사이드 수정
    @PutMapping("/{sideName}")
    public ResponseEntity<SideResponseDTO> updateSide(
            @PathVariable String sideName,
            @Valid @RequestPart("side") SideRequestDTO sideRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 업로드는 Service에서 처리
            SideResponseDTO response = sideService.editSideDetails(sideName, sideRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 사이드 삭제
    @DeleteMapping("/{sideName}")
    public void deleteSide(@PathVariable String sideName) {
        sideService.removeSide(sideName);
    }
}
