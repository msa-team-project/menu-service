package com.example.menuservice.controller;

import com.example.menuservice.dto.BreadRequestDTO;
import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.service.BreadService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/breads")
public class BreadApiController {

    private final BreadService breadService;
    private final FileUploadService fileUploadService;

    // 빵 목록 조회
    @GetMapping
    public Iterable<BreadResponseDTO> getBreads() {
        return breadService.viewBreadList();
    }

    // 빵 조회
    @GetMapping("/{breadName}")
    public BreadResponseDTO getBread(@PathVariable String breadName) {
        return breadService.viewBread(breadName);
    }

    // 빵 추가
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BreadResponseDTO> addBread(
            @RequestPart(value = "bread") String breadRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        System.out.println("Received bread data: " + breadRequestDTO);
        System.out.println("Received file name: " + file.getOriginalFilename());
//        try {
//            // ✅ 파일은 업로드하지 않고, Service에 그대로 넘긴다
//            BreadResponseDTO response = breadService.addBread(breadRequestDTO, file);
//            return ResponseEntity.ok(response);
//        } catch (IOException e) {
//            return ResponseEntity.badRequest().build();
//        }
        return null;
    }


    @PutMapping(value = "/{breadName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BreadResponseDTO> updateBread(
            @PathVariable String breadName,
            @Valid @RequestPart("bread") BreadRequestDTO breadRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ❌ Controller에서는 업로드 X
            // ✅ 파일만 그대로 전달
            BreadResponseDTO response = breadService.editBreadDetails(breadName, breadRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }



    // 빵 삭제
    @DeleteMapping("/{breadName}")
    public void deleteBread(@PathVariable String breadName) {
        breadService.removeBread(breadName);
    }

}
