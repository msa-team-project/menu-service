package com.example.menuservice.controller;

import com.example.menuservice.dto.CheeseRequestDTO;
import com.example.menuservice.dto.CheeseResponseDTO;
import com.example.menuservice.service.CheeseService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheeses")
public class CheeseApiController {

    private final CheeseService cheeseService;
    private final FileUploadService fileUploadService;

    // 치즈 목록 조회
    @GetMapping
    public Iterable<CheeseResponseDTO> getCheeses() {
        return cheeseService.viewCheeseList();
    }

    // 치즈 조회
    @GetMapping("/{cheeseName}")
    public CheeseResponseDTO getCheese(@PathVariable String cheeseName) {
        return cheeseService.viewCheese(cheeseName);
    }

    // 치즈 추가
    @PostMapping
    public ResponseEntity<CheeseResponseDTO> addCheese(
            @Valid @RequestPart("cheese") CheeseRequestDTO cheeseRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String fileUrl = fileUploadService.uploadFile(file);
                cheeseRequestDTO.setImg(fileUrl); // DTO에 이미지 URL 저장
            }
            CheeseResponseDTO response = cheeseService.addCheese(cheeseRequestDTO,file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 치즈 수정
    @PutMapping("/{cheeseName}")
    public ResponseEntity<CheeseResponseDTO> updateCheese(
            @PathVariable String cheeseName,
            @Valid @RequestPart(name = "cheese", required = false) CheeseRequestDTO cheeseRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String fileUrl = fileUploadService.uploadFile(file);
                cheeseRequestDTO.setImg(fileUrl);
            }
            CheeseResponseDTO response = cheeseService.editCheeseDetails(cheeseName, cheeseRequestDTO,file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 치즈 삭제
    @DeleteMapping("/{cheeseName}")
    public void deleteCheese(@PathVariable String cheeseName) {
        cheeseService.removeCheese(cheeseName);
    }
}
