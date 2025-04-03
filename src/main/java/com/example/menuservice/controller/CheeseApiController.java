package com.example.menuservice.controller;

import com.example.menuservice.dto.CheeseRequestDTO;
import com.example.menuservice.dto.CheeseResponseDTO;
import com.example.menuservice.service.CheeseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cheeses")
public class CheeseApiController {

    private final CheeseService cheeseService;

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
    public CheeseResponseDTO addCheese(@Valid @RequestBody CheeseRequestDTO cheeseRequestDTO) {
        return cheeseService.addCheese(cheeseRequestDTO);
    }

    // 치즈 수정
    @PutMapping("/{cheeseName}")
    public CheeseResponseDTO updateCheese(@PathVariable String cheeseName, @Valid @RequestBody CheeseRequestDTO cheeseRequestDTO) {
        return cheeseService.editCheeseDetails(cheeseName, cheeseRequestDTO);
    }

    // 치즈 삭제
    @DeleteMapping("/{cheeseName}")
    public void deleteCheese(@PathVariable String cheeseName) {
        cheeseService.removeCheese(cheeseName);
    }
    //     상태 업데이트
    @PatchMapping("/{uid}/status")
    public void updateCheeseStatus(@PathVariable Long uid, @RequestParam String status) {
        cheeseService.updateCheeseStatus(uid, status);
    }
}
