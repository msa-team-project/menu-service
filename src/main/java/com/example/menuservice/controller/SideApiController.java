package com.example.menuservice.controller;

import com.example.menuservice.dto.SideRequestDTO;
import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.service.SideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sides")
public class SideApiController {

    private final SideService sideService;

    // 사이드 목록 조회
    @GetMapping
    public Iterable<SideResponseDTO> getSides() {
        return sideService.viewSideList();
    }

    // 사이드 이름으로 사이드 조회
    @GetMapping("/{sideName}")
    public SideResponseDTO getSide(@PathVariable String sideName) {
        return sideService.viewSide(sideName);
    }

    // 사이드 추가
    @PostMapping
    public SideResponseDTO addSide(@Valid @RequestBody SideRequestDTO sideRequestDTO) {
        return sideService.addSide(sideRequestDTO);
    }

    // 사이드 수정
    @PutMapping("/{sideName}")
    public SideResponseDTO updateSide(@PathVariable String sideName, @Valid @RequestBody SideRequestDTO sideRequestDTO) {
        return sideService.editSideDetails(sideName, sideRequestDTO);
    }

    // 사이드 삭제
    @DeleteMapping("/{sideName}")
    public void deleteSide(@PathVariable String sideName) {
        sideService.removeSide(sideName);
    }

    //     상태 업데이트
    @PatchMapping("/{uid}/status")
    public void updateBreadStatus(@PathVariable Long uid, @RequestParam String status) {
        sideService.updateSideStatus(uid, status);
    }
}
