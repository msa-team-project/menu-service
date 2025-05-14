package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.BreadStatus;
import com.example.menuservice.domain.Bread;
import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.exception.BreadAlreadyExistsException;
import com.example.menuservice.exception.BreadNotFoundException;
import com.example.menuservice.repository.BreadRepository;
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
public class BreadService {
    private final BreadRepository breadRepository;
    private final FileUploadService fileUploadService;
    private final RabbitTemplate rabbitTemplate; // RabbitMQ 직접 접근용

    // 빵 목록 조회
    public List<BreadResponseDTO> viewBreadList() {
        return breadRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 빵 이름으로 빵 조회
    public BreadResponseDTO viewBread(String breadName) {
        Bread bread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));
        return toResponseDTO(bread);
    }

    // 빵 추가
    @Transactional
    public BreadResponseDTO addBread(String breadRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(breadRequestJson);

            // JsonNode에서 필요한 값 꺼내기
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

//            breadRepository.save(bread);
            // 메시지 전송
            rabbitTemplate.convertAndSend("ingredient-add.menu-service",
                    IngredientEventDTO.builder()
                            .type("bread")
                            .id(bread.getUid())
                            .name(bread.getBreadName())
                            .status(bread.getStatus())
                            .eventType(EventType.CREATED)
                            .updatedAt(Instant.now())
                            .build());

            return toResponseDTO(bread);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 빵 수정
    @Transactional
    public BreadResponseDTO editBreadDetails(String breadName, String breadRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(breadRequestJson);

            // JSON에서 필요한 값 추출
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


//            breadRepository.save(bread);
            // 메시지 전송
            rabbitTemplate.convertAndSend("ingredient-update.menu-service",
                    IngredientEventDTO.builder()
                            .type("bread")
                            .id(bread.getUid())
                            .name(bread.getBreadName())
                            .status(bread.getStatus())
                            .eventType(EventType.UPDATED)
                            .updatedAt(Instant.now())
                            .build());

            return toResponseDTO(bread);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }

    }


    // 빵 삭제
    @Transactional
    public void removeBread(String breadName) {
        Bread bread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));
        bread.setStatus(BreadStatus.DELETED.name());
        breadRepository.save(bread);
    }






    // Bread -> BreadResponseDTO 변환 메서드
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
