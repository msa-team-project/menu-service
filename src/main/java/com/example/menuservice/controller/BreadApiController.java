package com.example.menuservice.controller;

import com.example.menuservice.dto.BreadRequestDTO;
import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.service.BreadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/breads")
public class BreadApiController {

    private final BreadService breadService;

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
    @PostMapping
    public BreadResponseDTO addBread(@Valid @RequestBody BreadRequestDTO breadRequestDTO) {
        return breadService.addBread(breadRequestDTO);
    }

    // 빵 수정
    @PutMapping("/{breadName}")
    public BreadResponseDTO updateBread(@PathVariable String breadName, @Valid @RequestBody BreadRequestDTO breadRequestDTO) {
        return breadService.editBreadDetails(breadName, breadRequestDTO);
    }

    // 빵 삭제
    @DeleteMapping("/{breadName}")
    public void deleteBread(@PathVariable String breadName) {
        breadService.removeBread(breadName);
    }

//    //     상태 업데이트
//    @PatchMapping("/{uid}/status")
//    public void updateBreadStatus(@PathVariable Long uid, @RequestParam String status) {
//        breadService.updateBreadStatus(uid, status);
//    }
}
