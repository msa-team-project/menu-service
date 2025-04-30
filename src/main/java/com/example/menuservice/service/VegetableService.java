package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.VegetableStatus;
import com.example.menuservice.domain.Vegetable;
import com.example.menuservice.dto.VegetableRequestDTO;
import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.exception.VegetableAlreadyExistsException;
import com.example.menuservice.exception.VegetableNotFoundException;
import com.example.menuservice.repository.VegetableRepository;
import com.example.menuservice.type.EventType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VegetableService {

    private final VegetableRepository vegetableRepository;
    private final FileUploadService fileUploadService;
    private final RabbitTemplate rabbitTemplate; // RabbitMQ 직접 접근용

    // 채소 목록 조회
    public List<VegetableResponseDTO> viewVegetableList() {
        return vegetableRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 채소 이름으로 채소 조회
    public VegetableResponseDTO viewVegetable(String vegetableName) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));
        return toResponseDTO(vegetable);
    }

    // 채소 추가
    @Transactional
    public VegetableResponseDTO addVegetable(String vegetableRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(vegetableRequestJson);

            // JSON에서 필요한 값 추출
            String vegetableName = jsonNode.get("vegetableName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            if (vegetableRepository.existsByVegetableName(vegetableName)) {
                throw new VegetableAlreadyExistsException(vegetableName);
            }

            VegetableStatus status = VegetableStatus.valueOf(statusStr.toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Vegetable vegetable = Vegetable.builder()
                    .vegetableName(vegetableName)
                    .calorie(calorie)
                    .price(price)
                    .status(status.name())
                    .img(fileUrl)
                    .build();

            vegetableRepository.save(vegetable);
            // 메시지 전송
            rabbitTemplate.convertAndSend("ingredient-add.menu-service",
                    IngredientEventDTO.builder()
                            .type("vegetable")
                            .id(vegetable.getUid())
                            .name(vegetable.getVegetableName())
                            .status(vegetable.getStatus())
                            .eventType(EventType.CREATED)
                            .updatedAt(Instant.now())
                            .build());

            return toResponseDTO(vegetable);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 채소 삭제 (상태만 DELETED로 변경)
    @Transactional
    public void removeVegetable(String vegetableName) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));
        vegetable.setStatus(VegetableStatus.DELETED.name());
        vegetableRepository.save(vegetable);
    }

    // 채소 수정
    @Transactional
    public VegetableResponseDTO editVegetableDetails(String vegetableName, String vegetableRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(vegetableRequestJson);

            // JSON에서 필요한 값 추출
            String vegetableNameFromJson = jsonNode.get("vegetableName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                    .orElseThrow(() -> new VegetableNotFoundException(vegetableName));

            fileUrl = vegetable.getImg();

            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            vegetable.updateVegetable(
                    vegetableNameFromJson,
                    calorie,
                    price,
                    fileUrl,
                    statusStr
            );

            vegetableRepository.save(vegetable);
            // 메시지 전송
            rabbitTemplate.convertAndSend("ingredient-update.menu-service",
                    IngredientEventDTO.builder()
                            .type("vegetable")
                            .id(vegetable.getUid())
                            .name(vegetable.getVegetableName())
                            .status(vegetable.getStatus())
                            .eventType(EventType.UPDATED)
                            .updatedAt(Instant.now())
                            .build());

            return toResponseDTO(vegetable);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // Vegetable -> VegetableResponseDTO 변환 메서드
    private VegetableResponseDTO toResponseDTO(Vegetable vegetable) {
        return new VegetableResponseDTO(
                vegetable.getUid(),
                vegetable.getVegetableName(),
                vegetable.getCalorie(),
                vegetable.getPrice(),
                vegetable.getImg(),
                vegetable.getStatus(),
                vegetable.getCreatedDate(),
                vegetable.getVersion()
        );
    }
}
