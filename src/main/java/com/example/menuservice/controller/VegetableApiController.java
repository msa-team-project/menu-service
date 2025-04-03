package com.example.menuservice.controller;

import com.example.menuservice.dto.VegetableRequestDTO;
import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.service.VegetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vegetables")
public class VegetableApiController {

    private final VegetableService vegetableService;

    // 채소 목록 조회
    @GetMapping
    public Iterable<VegetableResponseDTO> getVegetables() {
        return vegetableService.viewVegetableList();
    }

    // 채소 이름으로 채소 조회
    @GetMapping("/{vegetableName}")
    public VegetableResponseDTO getVegetable(@PathVariable String vegetableName) {
        return vegetableService.viewVegetable(vegetableName);
    }

    // 채소 추가
    @PostMapping
    public VegetableResponseDTO addVegetable(@Valid @RequestBody VegetableRequestDTO vegetableRequestDTO) {
        return vegetableService.addVegetable(vegetableRequestDTO);
    }

    // 채소 수정
    @PutMapping("/{vegetableName}")
    public VegetableResponseDTO updateVegetable(@PathVariable String vegetableName, @Valid @RequestBody VegetableRequestDTO vegetableRequestDTO) {
        return vegetableService.editVegetableDetails(vegetableName, vegetableRequestDTO);
    }

    // 채소 삭제
    @DeleteMapping("/{vegetableName}")
    public void deleteVegetable(@PathVariable String vegetableName) {
        vegetableService.removeVegetable(vegetableName);
    }

    //     상태 업데이트
    @PatchMapping("/{uid}/status")
    public void updateVegetableStatus(@PathVariable Long uid, @RequestParam String status) {
       vegetableService.updateVegetableStatus(uid, status);
    }
}
