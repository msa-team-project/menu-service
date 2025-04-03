package com.example.menuservice.controller;

import com.example.menuservice.dto.store.StoreListRequestDTO;
import com.example.menuservice.dto.store.StoreListResponseDTO;
import com.example.menuservice.dto.store.StoreRequestDTO;
import com.example.menuservice.dto.store.StoreResponseDTO;
import com.example.menuservice.exception.StoreAlreadyExistsException;
import com.example.menuservice.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreApiController {

    private final StoreService storeService;

    //지점 목록 조회
    @GetMapping
    public StoreListResponseDTO getAllStores(@RequestBody StoreListRequestDTO storeListRequestDTO){
        // 지점 목록 가져오기
        return  storeService.getStoresByCursor(storeListRequestDTO.getLastUid(), storeListRequestDTO.getLimit());
    }

    //지점 uid로 지점 조회
    @GetMapping("/{uid}")
    public StoreResponseDTO getStore(@PathVariable("uid") Long uid) {
        return storeService.viewStore(uid);
    }

    //지점 추가
    @PostMapping
    public StoreResponseDTO addStore(@Valid @RequestBody StoreRequestDTO storeRequestDTO) throws StoreAlreadyExistsException {
        return storeService.addStore(storeRequestDTO);
    }

    //지점 삭제
    @DeleteMapping("/{uid}")
    public void deleteStore(@PathVariable("uid") Long uid) {
        storeService.deleteStore(uid);
    }

    //지점 상태 업데이트
    @PatchMapping("/{uid}")
    public void updateStatusByUid(@PathVariable("uid") Long uid, @RequestParam("storeStatus") String storeStatus) {
        storeService.updateStatusStore(uid, storeStatus);
    }

}
