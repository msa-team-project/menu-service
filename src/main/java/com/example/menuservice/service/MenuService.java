package com.example.menuservice.service;

import com.example.menuservice.domain.*;
import com.example.menuservice.dto.MenuResponseDTO;
import com.example.menuservice.event.MenuEventDTO;
import com.example.menuservice.exception.MenuAlreadyExistsException;
import com.example.menuservice.exception.MenuNotFoundException;
import com.example.menuservice.mapper.MenuMapper;
import com.example.menuservice.repository.*;
import com.example.menuservice.sqs.SqsConfig;
import com.example.menuservice.status.MenuStatus;
import com.example.menuservice.type.EventType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

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

    private final ObjectMapper objectMapper;
    private final SqsClient sqsClient;
    private final SqsConfig sqsConfig;

    // 메뉴 목록 조회
    public List<MenuResponseDTO> viewMenuList() {
        return menuRepository.findAll().stream()
                .map(MenuMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 단일 메뉴 조회
    public MenuResponseDTO viewMenu(String menuName) {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));
        return MenuMapper.toResponseDTO(menu);
    }

    @Transactional
    public MenuResponseDTO addMenu(@Valid String menuRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;
        JsonNode menuJson = objectMapper.readTree(menuRequestJson);

        String menuName = menuJson.get("menuName").asText();
        if (menuRepository.existsByMenuName(menuName)) {
            throw new MenuAlreadyExistsException(menuName);
        }

        try {
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Menu menu = buildMenuFromJson(menuJson, fileUrl);

            sendMenuEvent(menu, EventType.CREATED);

            return MenuMapper.toResponseDTO(menu);
        } catch (Exception e) {
            deleteUploadedFileSafely(fileUrl);
            throw e;
        }
    }

    @Transactional
    public MenuResponseDTO editMenuDetails(String menuName, @Valid String menuRequestJson, MultipartFile file) throws IOException {
        Menu menu = menuRepository.findByMenuName(menuName)
                .orElseThrow(() -> new MenuNotFoundException(menuName));

        String fileUrl = menu.getImg();

        try {
            JsonNode menuJson = objectMapper.readTree(menuRequestJson);

            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            menu.updateMenu(
                    menuJson.get("menuName").asText(),
                    menuJson.get("price").asLong(),
                    menuJson.get("calorie").asDouble(),
                    getBread(menuJson.get("bread").asLong()),
                    getMaterial(menuJson.get("material1").asLong()),
                    getOptionalEntity(menuJson, "material2", this::getMaterial),
                    getOptionalEntity(menuJson, "material3", this::getMaterial),
                    getOptionalEntity(menuJson, "cheese", this::getCheese),
                    getVegetable(menuJson.get("vegetable1").asLong()),
                    getOptionalEntity(menuJson, "vegetable2", this::getVegetable),
                    getOptionalEntity(menuJson, "vegetable3", this::getVegetable),
                    getOptionalEntity(menuJson, "vegetable4", this::getVegetable),
                    getOptionalEntity(menuJson, "vegetable5", this::getVegetable),
                    getOptionalEntity(menuJson, "vegetable6", this::getVegetable),
                    getOptionalEntity(menuJson, "vegetable7", this::getVegetable),
                    getOptionalEntity(menuJson, "vegetable8", this::getVegetable),
                    getSauce(menuJson.get("sauce1").asLong()),
                    getOptionalEntity(menuJson, "sauce2", this::getSauce),
                    getOptionalEntity(menuJson, "sauce3", this::getSauce),
                    fileUrl,
                    menuJson.get("status").asText()
            );

            sendMenuEvent(menu, EventType.UPDATED);

            return MenuMapper.toResponseDTO(menu);

        } catch (Exception e) {
            deleteUploadedFileSafely(fileUrl);
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

    // === 내부 도우미 메서드 ===
    private Bread getBread(Long id) {
        return breadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Bread not found: " + id));
    }

    private Material getMaterial(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Material not found: " + id));
    }

    private Cheese getCheese(Long id) {
        return cheeseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cheese not found: " + id));
    }

    private Vegetable getVegetable(Long id) {
        return vegetableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Vegetable not found: " + id));
    }

    private Sauce getSauce(Long id) {
        return sauceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sauce not found: " + id));
    }

    private Menu buildMenuFromJson(JsonNode json, String fileUrl) {
        MenuStatus status = MenuStatus.valueOf(json.get("status").asText().toUpperCase());

        return Menu.builder()
                .menuName(json.get("menuName").asText())
                .price(json.get("price").asLong())
                .calorie(json.get("calorie").asDouble())
                .bread(getBread(json.get("bread").asLong()))
                .material1(getMaterial(json.get("material1").asLong()))
                .material2(getOptionalEntity(json, "material2", this::getMaterial))
                .material3(getOptionalEntity(json, "material3", this::getMaterial))
                .cheese(getOptionalEntity(json, "cheese", this::getCheese))
                .vegetable1(getVegetable(json.get("vegetable1").asLong()))
                .vegetable2(getOptionalEntity(json, "vegetable2", this::getVegetable))
                .vegetable3(getOptionalEntity(json, "vegetable3", this::getVegetable))
                .vegetable4(getOptionalEntity(json, "vegetable4", this::getVegetable))
                .vegetable5(getOptionalEntity(json, "vegetable5", this::getVegetable))
                .vegetable6(getOptionalEntity(json, "vegetable6", this::getVegetable))
                .vegetable7(getOptionalEntity(json, "vegetable7", this::getVegetable))
                .vegetable8(getOptionalEntity(json, "vegetable8", this::getVegetable))
                .sauce1(getSauce(json.get("sauce1").asLong()))
                .sauce2(getOptionalEntity(json, "sauce2", this::getSauce))
                .sauce3(getOptionalEntity(json, "sauce3", this::getSauce))
                .img(fileUrl)
                .status(status.name())
                .build();
    }

    private <T> T getOptionalEntity(JsonNode json, String field, java.util.function.Function<Long, T> fetcher) {
        JsonNode node = json.get(field);
        if (node == null || node.isNull()) return null;
        long id = node.asLong();
        return (id == 0L) ? null : fetcher.apply(id);
    }

    private void sendMenuEvent(Menu menu, EventType type) {
        try {
//            MenuEventDTO event = MenuEventDTO.builder()
//                    .menuId(menu.getUid())
//                    .menuName(menu.getMenuName())
//                    .status(menu.getStatus())
//                    .eventType(type)
//                    .updatedAt(Instant.now())
//                    .build();

            String messageBody = objectMapper.writeValueAsString(menu);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(sqsConfig.getQueueUrl())
                    .messageBody(messageBody)
                    .build();

            sqsClient.sendMessage(request);
        } catch (Exception e) {
            throw new RuntimeException("SQS 전송 실패", e);
        }
    }

    private void deleteUploadedFileSafely(String fileUrl) {
        if (fileUrl != null && !fileUrl.isEmpty()) {
            try {
                fileUploadService.deleteFile(fileUrl);
            } catch (Exception ignore) {
                // 로깅만 해도 충분
            }
        }
    }
}
