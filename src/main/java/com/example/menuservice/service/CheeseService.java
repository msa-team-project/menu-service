package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.CheeseStatus;
import com.example.menuservice.domain.Cheese;
import com.example.menuservice.dto.CheeseResponseDTO;
import com.example.menuservice.exception.CheeseAlreadyExistsException;
import com.example.menuservice.exception.CheeseNotFoundException;
import com.example.menuservice.repository.CheeseRepository;
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
public class CheeseService {

    private final CheeseRepository cheeseRepository;
    private final FileUploadService fileUploadService;
    private final SqsService sqsService;
    private final ObjectMapper objectMapper;

    // 치즈 목록 조회
    public List<CheeseResponseDTO> viewCheeseList() {
        return cheeseRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 치즈 이름으로 조회
    public CheeseResponseDTO viewCheese(String cheeseName) {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));
        return toResponseDTO(cheese);
    }

    // 치즈 추가
    @Transactional
    public CheeseResponseDTO addCheese(String cheeseRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(cheeseRequestJson);

            String cheeseName = jsonNode.get("cheeseName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            if (cheeseRepository.existsByCheeseName(cheeseName)) {
                throw new CheeseAlreadyExistsException(cheeseName);
            }

            CheeseStatus status = CheeseStatus.valueOf(statusStr.toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Cheese cheese = Cheese.builder()
                    .cheeseName(cheeseName)
                    .calorie(calorie)
                    .price(price)
                    .status(status.name())
                    .img(fileUrl)
                    .build();



            // SQS 메시지 전송 (추가 이벤트)
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("cheese")
                    .name(cheese.getCheeseName())
                    .calorie(cheese.getCalorie())
                    .price(cheese.getPrice())
                    .status(cheese.getStatus())
                    .img(cheese.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-add-menu-service");

            return toResponseDTO(cheese);

        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 치즈 수정
    @Transactional
    public CheeseResponseDTO editCheeseDetails(String cheeseName, String cheeseRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(cheeseRequestJson);

            String cheeseNameFromJson = jsonNode.get("cheeseName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                    .orElseThrow(() -> new CheeseNotFoundException(cheeseName));

            fileUrl = cheese.getImg();

            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            cheese.updateCheese(
                    cheeseNameFromJson,
                    calorie,
                    price,
                    fileUrl,
                    statusStr
            );



            // SQS 메시지 전송 (수정 이벤트)
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("cheese")
                    .id(cheese.getUid())
                    .name(cheese.getCheeseName())
                    .price(cheese.getPrice())
                    .calorie(cheese.getCalorie())
                    .status(cheese.getStatus())
                    .img(cheese.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-update-menu-service");

            return toResponseDTO(cheese);

        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 치즈 삭제 (상태만 DELETED로 변경)
    @Transactional
    public void removeCheese(String cheeseName) {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));
        cheese.setStatus(CheeseStatus.DELETED.name());


        // SQS 메시지 전송 (삭제 이벤트)
        try {
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("cheese")
                    .id(cheese.getUid())
                    .name(cheese.getCheeseName())
                    .price(cheese.getPrice())
                    .calorie(cheese.getCalorie())
                    .status(cheese.getStatus())
                    .img(cheese.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-delete-menu-service");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send delete message to SQS", e);
        }
    }

    // Cheese -> CheeseResponseDTO 변환 메서드
    private CheeseResponseDTO toResponseDTO(Cheese cheese) {
        return new CheeseResponseDTO(
                cheese.getUid(),
                cheese.getCheeseName(),
                cheese.getCalorie(),
                cheese.getPrice(),
                cheese.getImg(),
                cheese.getStatus(),
                cheese.getCreatedDate(),
                cheese.getVersion()
        );
    }
}
