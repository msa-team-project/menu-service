package com.example.menuservice.controller;

import com.example.menuservice.dto.CheeseResponseDTO;
import com.example.menuservice.exception.CheeseAlreadyExistsException;
import com.example.menuservice.exception.CheeseNotFoundException;
import com.example.menuservice.service.CheeseService;
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
@RequestMapping("/menus/cheeses")
public class CheeseApiController {

    private final CheeseService cheeseService;
    private final FileUploadService fileUploadService;

    @GetMapping
    public ResponseEntity<List<CheeseResponseDTO>> getCheeses() {
        return ResponseEntity.ok(cheeseService.viewCheeseList());
    }

    @GetMapping("/{cheeseName}")
    public ResponseEntity<?> getCheese(@PathVariable String cheeseName) {
        try {
            return ResponseEntity.ok(cheeseService.viewCheese(cheeseName));
        } catch (CheeseNotFoundException e) {
            log.warn("치즈를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCheese(
            @Valid @RequestPart("cheese") String cheeseRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            CheeseResponseDTO response = cheeseService.addCheese(cheeseRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (CheeseAlreadyExistsException e) {
            log.warn("중복된 치즈 이름: {}", e.getMessage());
            return ResponseEntity.status(409).body("이미 존재하는 치즈입니다.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("치즈 추가 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    @PutMapping(value = "/{cheeseName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCheese(
            @PathVariable String cheeseName,
            @Valid @RequestPart("cheese") String cheeseRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            CheeseResponseDTO response = cheeseService.editCheeseDetails(cheeseName, cheeseRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (CheeseNotFoundException e) {
            log.warn("수정할 치즈를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("치즈 수정 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    @DeleteMapping("/{cheeseName}")
    public ResponseEntity<?> deleteCheese(@PathVariable String cheeseName) {
        try {
            cheeseService.removeCheese(cheeseName);
            return ResponseEntity.noContent().build();
        } catch (CheeseNotFoundException e) {
            log.warn("삭제할 치즈를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("치즈 삭제 중 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }
}
