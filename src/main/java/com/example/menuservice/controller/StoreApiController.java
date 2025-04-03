package com.example.menuservice.controller;

import com.example.menuservice.dto.StoreRequestDTO;
import com.example.menuservice.dto.StoreResponseDTO;
import com.example.menuservice.exception.StoreAlreadyExistsException;
import com.example.menuservice.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreApiController {

    private final StoreService storeService;

    //지점 목록 조회
    @GetMapping
    public Iterable<StoreResponseDTO> getAllStores() {
        return  storeService.viewStoreList();
    }

    //지점 uid로 지점 조회
    @GetMapping("/{uid}")
    public StoreResponseDTO getStore(@PathVariable("uid") int uid) {
        return storeService.viewStore(uid);
    }

    //지점 추가
    @PostMapping
    public StoreResponseDTO addStore(@Valid @RequestBody StoreRequestDTO storeRequestDTO) throws StoreAlreadyExistsException {
        return storeService.addStore(storeRequestDTO);
    }

    //지점 삭제
    @DeleteMapping("/{uid}")
    public void deleteStore(@PathVariable("uid") int uid) {
        storeService.deleteStore(uid);
    }

    //지점 상태 업데이트
    @PatchMapping("/{uid}")
    public void updateStatusByUid(@PathVariable("uid") int uid, @RequestParam("status") String status) {
        storeService.updateStatusStore(uid, status);
    }





}
