package com.example.menuservice.controller;

import com.example.menuservice.dto.MenuRequestDTO;
import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuApiController {

    private final MenuService menuService;

    // 메뉴 목록 조회
    @GetMapping
    public Iterable<MenuResponseDTO> getMenus() {
        return menuService.viewMenuList();
    }

    // 메뉴 이름으로 메뉴 조회
    @GetMapping("/{menuName}")
    public MenuResponseDTO getMenu(@PathVariable String menuName) {
        return menuService.viewMenu(menuName);
    }

    // 메뉴 추가
    @PostMapping
    public MenuResponseDTO addMenu(@Valid @RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.addMenu(menuRequestDTO);
    }

    // 메뉴 수정
    @PutMapping("/{menuName}")
    public MenuResponseDTO updateMenu(@PathVariable String menuName, @Valid @RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.editMenuDetails(menuName, menuRequestDTO);
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuName}")
    public void deleteMenu(@PathVariable String menuName) {
        menuService.removeMenu(menuName);
    }

    // 메뉴 상태 업데이트
    @PatchMapping("/{uid}/status")
    public void updateMenuStatus(@PathVariable Long uid, @RequestParam String status) {
        menuService.updateMenuStatus(uid, status);
    }
}

//메뉴 매핑 안됨
