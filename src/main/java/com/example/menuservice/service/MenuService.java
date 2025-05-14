package com.example.menuservice.service;

import com.example.menuservice.domain.*;
import com.example.menuservice.dto.MenuResponseDTO;

import com.example.menuservice.event.MenuEventDTO;
import com.example.menuservice.exception.MenuAlreadyExistsException;
import com.example.menuservice.exception.MenuNotFoundException;
import com.example.menuservice.mapper.MenuMapper;
import com.example.menuservice.repository.*;
import com.example.menuservice.status.MenuStatus;
import com.example.menuservice.type.EventType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final FileUploadService fileUploadService;
    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;

    private final ObjectMapper objectMapper; // ObjectMapper 인스턴스
    private final RabbitTemplate rabbitTemplate; // RabbitMQ 직접 접근용

    // 메뉴 목록 조회
    public List<MenuResponseDTO> viewMenuList() {
        return menuRepository.findAll().stream()
                .map(MenuMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 메뉴 이름으로 소스 조회
    public MenuResponseDTO viewMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        return MenuMapper.toResponseDTO(menu);
    }

    @Transactional
    public MenuResponseDTO addMenu(@Valid String menuRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        // JSON 데이터를 ObjectMapper로 파싱
        JsonNode menuJson = objectMapper.readTree(menuRequestJson);

        // Menu 이름으로 존재 여부 확인
        String menuName = menuJson.get("menuName").asText();
        if (menuRepository.existsByMenuName(menuName)) {
            throw new MenuAlreadyExistsException(menuName);
        }

        try {
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            MenuStatus status = MenuStatus.valueOf(menuJson.get("status").asText().toUpperCase());

            // 메뉴 엔티티 생성
            Menu menu = Menu.builder()
                    .menuName(menuName)
                    .price(menuJson.get("price").asLong())
                    .calorie(menuJson.get("calorie").asDouble())
                    .bread(getBread(menuJson.get("bread").asLong()))
                    .material1(getMaterial(menuJson.get("material1").asLong()))
                    .material2(getOptionalMaterial(menuJson.get("material2").asLong()))
                    .material3(getOptionalMaterial(menuJson.get("material3").asLong()))
                    .cheese(getOptionalCheese(menuJson.get("cheese").asLong()))
                    .vegetable1(getVegetable(menuJson.get("vegetable1").asLong()))
                    .vegetable2(getOptionalVegetable(menuJson.get("vegetable2").asLong()))
                    .vegetable3(getOptionalVegetable(menuJson.get("vegetable3").asLong()))
                    .vegetable4(getOptionalVegetable(menuJson.get("vegetable4").asLong()))
                    .vegetable5(getOptionalVegetable(menuJson.get("vegetable5").asLong()))
                    .vegetable6(getOptionalVegetable(menuJson.get("vegetable6").asLong()))
                    .vegetable7(getOptionalVegetable(menuJson.get("vegetable7").asLong()))
                    .vegetable8(getOptionalVegetable(menuJson.get("vegetable8").asLong()))
                    .sauce1(getSauce(menuJson.get("sauce1").asLong()))
                    .sauce2(getOptionalSauce(menuJson.get("sauce2").asLong()))
                    .sauce3(getOptionalSauce(menuJson.get("sauce3").asLong()))
                    .img(fileUrl)
                    .status(status.name())
                    .build();


//            menuRepository.save(menu);
            // 메시지 전송
            rabbitTemplate.convertAndSend("menu-add.menu-service",
                    MenuEventDTO.builder()

                            .menuId(menu.getUid())
                            .menuName(menu.getMenuName())
                            .status(menu.getStatus())
                            .eventType(EventType.CREATED)
                            .updatedAt(Instant.now())
                            .build());

            return MenuMapper.toResponseDTO(menu);



        } catch (Exception e) {
            // 파일 업로드 실패 시 파일 삭제
            if (fileUrl != null) fileUploadService.deleteFile(fileUrl);
            throw e;
        }

    }

    @Transactional
    public MenuResponseDTO editMenuDetails(String menuName, @Valid String menuRequestJson, MultipartFile file) throws IOException {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));

         String fileUrl = menu.getImg();

        try {
            // JSON 데이터를 ObjectMapper로 파싱
            JsonNode menuJson = objectMapper.readTree(menuRequestJson);

            // 새로운 파일이 업로드된 경우에만 처리
            if (file != null && !file.isEmpty()) {
                // 기존 이미지가 있다면 삭제
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);  // 기존 이미지 삭제
                }
                // 새로운 파일 업로드
                fileUrl = fileUploadService.uploadFile(file);
            }

            // 메뉴 정보 업데이트
            menu.updateMenu(
                    menuJson.get("menuName").asText(),
                    menuJson.get("price").asLong(),
                    menuJson.get("calorie").asDouble(),
                    getBread(menuJson.get("bread").asLong()),
                    getMaterial(menuJson.get("material1").asLong()),
                    getOptionalMaterial(menuJson.get("material2").asLong()),
                    getOptionalMaterial(menuJson.get("material3").asLong()),
                    getOptionalCheese(menuJson.get("cheese").asLong()),
                    getVegetable(menuJson.get("vegetable1").asLong()),
                    getOptionalVegetable(menuJson.get("vegetable2").asLong()),
                    getOptionalVegetable(menuJson.get("vegetable3").asLong()),
                    getOptionalVegetable(menuJson.get("vegetable4").asLong()),
                    getOptionalVegetable(menuJson.get("vegetable5").asLong()),
                    getOptionalVegetable(menuJson.get("vegetable6").asLong()),
                    getOptionalVegetable(menuJson.get("vegetable7").asLong()),
                    getOptionalVegetable(menuJson.get("vegetable8").asLong()),
                    getSauce(menuJson.get("sauce1").asLong()),
                    getOptionalSauce(menuJson.get("sauce2").asLong()),
                    getOptionalSauce(menuJson.get("sauce3").asLong()),
                    fileUrl,  // 업데이트된 이미지 URL
                    menuJson.get("status").asText()

            );


//            menuRepository.save(menu);
            // 메시지 전송
            rabbitTemplate.convertAndSend("menu-update.menu-service",
                    MenuEventDTO.builder()

                            .menuId(menu.getUid())
                            .menuName(menu.getMenuName())
                            .status(menu.getStatus())
                            .eventType(EventType.UPDATED)
                            .updatedAt(Instant.now())
                            .build());

            return MenuMapper.toResponseDTO(menu);

        } catch (Exception e) {
            // 예외 발생 시 기존 이미지 삭제
            if (fileUrl != null && !fileUrl.isEmpty()) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }

    }


    @Transactional
    public void removeMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        menu.setStatus(MenuStatus.DELETED.name());
    }

    @Transactional
    public void updateMenuStatus(Long uid, String status) {
        Menu menu = menuRepository.findById(uid)
                .orElseThrow(() -> new MenuNotFoundException("ID: " + uid));
        menu.setStatus(status);
    }

    // === Entity fetch helpers ===
    private Bread getBread(Long id) {
        return breadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Bread not found: " + id));
    }

    private Material getMaterial(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Material not found: " + id));
    }

    private Material getOptionalMaterial(Long id) {
        return id != null && id != 0 ? getMaterial(id) : null;
    }

    private Cheese getOptionalCheese(Long id) {
        return id != null && id != 0 ? cheeseRepository.findById(id).orElse(null) : null;
    }

    private Vegetable getVegetable(Long id) {
        return vegetableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Vegetable not found: " + id));
    }

    private Vegetable getOptionalVegetable(Long id) {
        return id != null && id != 0 ? getVegetable(id) : null;
    }

    private Sauce getSauce(Long id) {
        return sauceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sauce not found: " + id));
    }

    private Sauce getOptionalSauce(Long id) {
        return id != null && id != 0 ? getSauce(id) : null;
    }
}
