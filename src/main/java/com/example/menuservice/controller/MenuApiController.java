package com.example.menuservice.controller;

import com.example.menuservice.dto.MenuRequestDTO;
import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.service.MenuService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuApiController {

    private final MenuService menuService;
    private final FileUploadService fileUploadService;

    // 메뉴 목록 조회
    @GetMapping
    public Iterable<MenuResponseDTO> getMenus() {
        return menuService.viewMenuList();
    }

    // 메뉴 조회
    @GetMapping("/{menuName}")
    public MenuResponseDTO getMenu(@PathVariable String menuName) {
        return menuService.viewMenu(menuName);
    }

    // 메뉴 추가
    @PostMapping
    public ResponseEntity<MenuResponseDTO> addMenu(
            @Valid @RequestPart("menu") MenuRequestDTO menuRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 파일 업로드는 Service에서 수행
            MenuResponseDTO response = menuService.addMenu(menuRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 메뉴 수정
    @PutMapping("/{menuName}")
    public ResponseEntity<MenuResponseDTO> updateMenu(
            @PathVariable String menuName,
            @Valid @RequestPart("menu") MenuRequestDTO menuRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // ✅ 파일 업로드는 Service에서 수행
            MenuResponseDTO response = menuService.editMenuDetails(menuName, menuRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuName}")
    public void deleteMenu(@PathVariable String menuName) {
        menuService.removeMenu(menuName);
    }
}
