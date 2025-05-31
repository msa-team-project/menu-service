package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.VegetableStatus;
import com.example.menuservice.domain.Vegetable;
import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.exception.VegetableAlreadyExistsException;
import com.example.menuservice.exception.VegetableNotFoundException;
import com.example.menuservice.repository.VegetableRepository;
import com.example.menuservice.sqs.SqsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VegetableService {

    private final VegetableRepository vegetableRepository;
    private final FileUploadService fileUploadService;
    private final SqsService sqsService;
    private final ObjectMapper objectMapper;

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
            JsonNode jsonNode = objectMapper.readTree(vegetableRequestJson);

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



            // SQS 메시지 전송 (추가 이벤트)
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("vegetable")
                    .name(vegetable.getVegetableName())
                    .calorie(vegetable.getCalorie())
                    .price(vegetable.getPrice())
                    .status(vegetable.getStatus())
                    .img(vegetable.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-add-menu-service");

            return toResponseDTO(vegetable);

        } catch (Exception e) {
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                } catch (Exception ignored) {}
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

        // SQS 메시지 전송 (삭제 이벤트)
        try {
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("vegetable")
                    .id(vegetable.getUid())
                    .name(vegetable.getVegetableName())
                    .price(vegetable.getPrice())
                    .calorie(vegetable.getCalorie())
                    .status(vegetable.getStatus())
                    .img(vegetable.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-delete-menu-service");

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send delete message to SQS", e);
        }
    }

    // 채소 수정
    @Transactional
    public VegetableResponseDTO editVegetableDetails(String vegetableName, String vegetableRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(vegetableRequestJson);

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
                    VegetableStatus.valueOf(statusStr.toUpperCase()).name()
            );



            // SQS 메시지 전송 (수정 이벤트)
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("vegetable")
                    .id(vegetable.getUid())
                    .name(vegetable.getVegetableName())
                    .price(vegetable.getPrice())
                    .calorie(vegetable.getCalorie())
                    .status(vegetable.getStatus())
                    .img(vegetable.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-update-menu-service");

            return toResponseDTO(vegetable);

        } catch (Exception e) {
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                } catch (Exception ignored) {}
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
