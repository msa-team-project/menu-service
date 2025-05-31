package com.example.menuservice.service;

import com.example.menuservice.domain.Bread;
import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.exception.BreadAlreadyExistsException;
import com.example.menuservice.exception.BreadNotFoundException;
import com.example.menuservice.repository.BreadRepository;
import com.example.menuservice.sqs.SqsService;
import com.example.menuservice.status.BreadStatus;
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
public class BreadService {

    private final BreadRepository breadRepository;
    private final FileUploadService fileUploadService;
    private final SqsService sqsService;
    private final ObjectMapper objectMapper;

    public List<BreadResponseDTO> viewBreadList() {
        return breadRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BreadResponseDTO viewBread(String breadName) {
        Bread bread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));
        return toResponseDTO(bread);
    }

    @Transactional
    public BreadResponseDTO addBread(String breadRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(breadRequestJson);

            String breadName = jsonNode.get("breadName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            if (breadRepository.existsByBreadName(breadName)) {
                throw new BreadAlreadyExistsException(breadName);
            }

            BreadStatus status = BreadStatus.valueOf(statusStr.toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Bread bread = Bread.builder()
                    .breadName(breadName)
                    .calorie(calorie)
                    .price(price)
                    .status(status.name())
                    .img(fileUrl)
                    .build();



            // SQS 메시지 전송
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("bread")
                    .name(bread.getBreadName())
                    .calorie(bread.getCalorie())
                    .price(bread.getPrice())
                    .status(bread.getStatus())
                    .img(bread.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-add-menu-service");

            return toResponseDTO(bread);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    @Transactional
    public BreadResponseDTO editBreadDetails(String breadName, String breadRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(breadRequestJson);

            String breadNameFromJson = jsonNode.get("breadName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            Bread bread = breadRepository.findByBreadName(breadName)
                    .orElseThrow(() -> new BreadNotFoundException(breadName));

            fileUrl = bread.getImg();

            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            bread.updateBread(
                    breadNameFromJson,
                    calorie,
                    price,
                    fileUrl,
                    statusStr
            );



            // SQS 메시지 전송
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("bread")
                    .id(bread.getUid())
                    .name(bread.getBreadName())
                    .price(bread.getPrice())
                    .calorie(bread.getCalorie())
                    .status(bread.getStatus())
                    .img(bread.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-update-menu-service");

            return toResponseDTO(bread);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    @Transactional
    public void removeBread(String breadName) {
        Bread bread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));

        bread.setStatus(BreadStatus.DELETED.name());


        // SQS 메시지 전송
        try {
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("bread")
                    .id(bread.getUid())
                    .name(bread.getBreadName())
                    .price(bread.getPrice())
                    .calorie(bread.getCalorie())
                    .status(bread.getStatus())
                    .img(bread.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-delete-menu-service");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send delete message to SQS", e);
        }
    }

    private BreadResponseDTO toResponseDTO(Bread bread) {
        return new BreadResponseDTO(
                bread.getUid(),
                bread.getBreadName(),
                bread.getCalorie(),
                bread.getPrice(),
                bread.getImg(),
                bread.getStatus(),
                bread.getCreatedDate(),
                bread.getVersion()
        );
    }
}






