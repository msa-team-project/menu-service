package com.example.menuservice.controller;

import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.exception.MenuAlreadyExistsException;
import com.example.menuservice.exception.MenuNotFoundException;
import com.example.menuservice.service.MenuService;
import com.example.menuservice.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuApiController {

    private final MenuService menuService;
    private final FileUploadService fileUploadService;

    // 메뉴 목록 조회
    @GetMapping
    public ResponseEntity<Iterable<MenuResponseDTO>> getMenus() {
        return ResponseEntity.ok(menuService.viewMenuList());
    }

    // 메뉴 조회
    @GetMapping("/{menuName}")
    public ResponseEntity<?> getMenu(@PathVariable String menuName) {
        try {
            MenuResponseDTO response = menuService.viewMenu(menuName);
            return ResponseEntity.ok(response);
        } catch (MenuNotFoundException e) {
            log.warn("메뉴를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // 메뉴 추가
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMenu(
            @Valid @RequestPart("menu") String menuRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            MenuResponseDTO response = menuService.addMenu(menuRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (MenuAlreadyExistsException e) {
            log.warn("중복된 메뉴 이름: {}", e.getMessage());
            return ResponseEntity.status(409).body("이미 존재하는 메뉴입니다.");
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("메뉴 추가 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 메뉴 수정
    @PutMapping(value = "/{menuName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMenu(
            @PathVariable String menuName,
            @Valid @RequestPart("menu") String menuRequestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            MenuResponseDTO response = menuService.editMenuDetails(menuName, menuRequestDTO, file);
            return ResponseEntity.ok(response);
        } catch (MenuNotFoundException e) {
            log.warn("수정할 메뉴를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("파일 업로드 중 오류", e);
            return ResponseEntity.badRequest().body("파일 업로드 실패");
        } catch (Exception e) {
            log.error("메뉴 수정 중 알 수 없는 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuName}")
    public ResponseEntity<?> deleteMenu(@PathVariable String menuName) {
        try {
            menuService.removeMenu(menuName);
            return ResponseEntity.noContent().build();
        } catch (MenuNotFoundException e) {
            log.warn("삭제할 메뉴를 찾을 수 없습니다: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("메뉴 삭제 중 오류", e);
            return ResponseEntity.internalServerError().body("서버 오류");
        }
    }
}
