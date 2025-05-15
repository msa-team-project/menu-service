package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.SideStatus;
import com.example.menuservice.domain.Side;
import com.example.menuservice.dto.SideRequestDTO;
import com.example.menuservice.dto.SideResponseDTO;
import com.example.menuservice.exception.SideAlreadyExistsException;
import com.example.menuservice.exception.SideNotFoundException;
import com.example.menuservice.repository.SideRepository;
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
public class SideService {

    private final SideRepository sideRepository;
    private final FileUploadService fileUploadService;


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
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(sideRequestJson);

            // JSON에서 필요한 값 추출
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

           sideRepository.save(side);
            // 메시지 전송


            return toResponseDTO(side);


        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
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
        sideRepository.save(side);
    }

    // 사이드 수정
    @Transactional
    public SideResponseDTO editSideDetails(String sideName, String sideRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(sideRequestJson);

            // JSON에서 필요한 값 추출
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
                    statusStr
            );

            return toResponseDTO(sideRepository.save(side));
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
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
