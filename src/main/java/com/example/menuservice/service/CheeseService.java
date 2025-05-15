package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.CheeseStatus;
import com.example.menuservice.domain.Cheese;
import com.example.menuservice.dto.CheeseRequestDTO;
import com.example.menuservice.dto.CheeseResponseDTO;
import com.example.menuservice.exception.CheeseAlreadyExistsException;
import com.example.menuservice.exception.CheeseNotFoundException;
import com.example.menuservice.repository.CheeseRepository;
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
public class CheeseService {

    private final CheeseRepository cheeseRepository;
    private final FileUploadService fileUploadService;

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
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(cheeseRequestJson);

            // JsonNode에서 필요한 값 꺼내기
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

            cheeseRepository.save(cheese);
            // 메시지 전송


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
        cheeseRepository.save(cheese);
    }

    // 치즈 수정
    @Transactional
    public CheeseResponseDTO editCheeseDetails(String cheeseName, String cheeseRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(cheeseRequestJson);

            // JSON에서 필요한 값 추출
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


           cheeseRepository.save(cheese);
            // 메시지 전송


            return toResponseDTO(cheese);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
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
