package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.SideStatus;
import com.example.menuservice.domain.Side;
import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.exception.SideAlreadyExistsException;
import com.example.menuservice.exception.SideNotFoundException;
import com.example.menuservice.repository.SideRepository;
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
public class SideService {

    private final SideRepository sideRepository;
    private final FileUploadService fileUploadService;
    private final SqsService sqsService;
    private final ObjectMapper objectMapper;

    // 사이드 목록 조회
    public List<SideResponseDTO> viewSideList() {
        return sideRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 사이드 이름으로 사이드 조회
    public SideResponseDTO viewSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        return toResponseDTO(side);
    }

    // 사이드 추가
    @Transactional
    public SideResponseDTO addSide(String sideRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(sideRequestJson);

            String sideName = jsonNode.get("sideName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            if (sideRepository.existsBySideName(sideName)) {
                throw new SideAlreadyExistsException(sideName);
            }

            SideStatus status = SideStatus.valueOf(statusStr.toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Side side = Side.builder()
                    .sideName(sideName)
                    .calorie(calorie)
                    .price(price)
                    .status(status.name())
                    .img(fileUrl)
                    .build();



            // SQS 메시지 전송 (추가 이벤트)
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("side")
                    .name(side.getSideName())
                    .calorie(side.getCalorie())
                    .price(side.getPrice())
                    .status(side.getStatus())
                    .img(side.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-add-menu-service");

            return toResponseDTO(side);

        } catch (Exception e) {
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                } catch (Exception ignored) {}
            }
            throw e;
        }
    }

    // 사이드 삭제 (상태만 DELETED로 변경)
    @Transactional
    public void removeSide(String sideName) {
        Side side = sideRepository.findBySideName(sideName)
                .orElseThrow(() -> new SideNotFoundException(sideName));
        side.setStatus(SideStatus.DELETED.name());


        // SQS 메시지 전송 (삭제 이벤트)
        try {
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("side")
                    .id(side.getUid())
                    .name(side.getSideName())
                    .price(side.getPrice())
                    .calorie(side.getCalorie())
                    .status(side.getStatus())
                    .img(side.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-delete-menu-service");

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to send delete message to SQS", e);
        }
    }

    // 사이드 수정
    @Transactional
    public SideResponseDTO editSideDetails(String sideName, String sideRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            JsonNode jsonNode = objectMapper.readTree(sideRequestJson);

            String sideNameFromJson = jsonNode.get("sideName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            Side side = sideRepository.findBySideName(sideName)
                    .orElseThrow(() -> new SideNotFoundException(sideName));

            fileUrl = side.getImg();

            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            side.updateSide(
                    sideNameFromJson,
                    calorie,
                    price,
                    fileUrl,
                    SideStatus.valueOf(statusStr.toUpperCase()).name()
            );



            // SQS 메시지 전송 (수정 이벤트)
            IngredientEventDTO event = IngredientEventDTO.builder()
                    .type("side")
                    .id(side.getUid())
                    .name(side.getSideName())
                    .price(side.getPrice())
                    .calorie(side.getCalorie())
                    .status(side.getStatus())
                    .img(side.getImg())
                    .build();

            sqsService.sendMessageToSqs(objectMapper.writeValueAsString(event), "ingredient-update-menu-service");

            return toResponseDTO(side);

        } catch (Exception e) {
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                } catch (Exception ignored) {}
            }
            throw e;
        }
    }

    // Side -> SideResponseDTO 변환 메서드
    private SideResponseDTO toResponseDTO(Side side) {
        return new SideResponseDTO(
                side.getUid(),
                side.getSideName(),
                side.getCalorie(),
                side.getPrice(),
                side.getImg(),
                side.getStatus(),
                side.getCreatedDate(),
                side.getVersion()
        );
    }
}
