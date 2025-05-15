package com.example.menuservice.service;

import com.example.menuservice.event.IngredientEventDTO;
import com.example.menuservice.status.SauceStatus;
import com.example.menuservice.domain.Sauce;
import com.example.menuservice.dto.SauceRequestDTO;
import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.exception.SauceAlreadyExistsException;
import com.example.menuservice.exception.SauceNotFoundException;
import com.example.menuservice.repository.SauceRepository;
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
public class SauceService {

    private final SauceRepository sauceRepository;
    private final FileUploadService fileUploadService;


    // 소스 목록 조회
    public List<SauceResponseDTO> viewSauceList() {
        return sauceRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 소스 이름으로 소스 조회
    public SauceResponseDTO viewSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));
        return toResponseDTO(sauce);
    }

    // 소스 추가
    @Transactional
    public SauceResponseDTO addSauce(String sauceRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(sauceRequestJson);

            // JSON에서 필요한 값 추출
            String sauceName = jsonNode.get("sauceName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            if (sauceRepository.existsBySauceName(sauceName)) {
                throw new SauceAlreadyExistsException(sauceName);
            }

            SauceStatus status = SauceStatus.valueOf(statusStr.toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Sauce sauce = Sauce.builder()
                    .sauceName(sauceName)
                    .calorie(calorie)
                    .price(price)
                    .status(status.name())
                    .img(fileUrl)
                    .build();

            sauceRepository.save(sauce);
            // 메시지 전송


            return toResponseDTO(sauce);


        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 소스 삭제 (상태만 DELETED로 변경)
    @Transactional
    public void removeSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));
        sauce.setStatus(SauceStatus.DELETED.name());
        sauceRepository.save(sauce);
    }

    // 소스 수정
    @Transactional
    public SauceResponseDTO editSauceDetails(String sauceName, String sauceRequestJson, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ObjectMapper로 JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(sauceRequestJson);

            // JSON에서 필요한 값 추출
            String sauceNameFromJson = jsonNode.get("sauceName").asText();
            Double calorie = jsonNode.get("calorie").asDouble();
            int price = jsonNode.get("price").asInt();
            String statusStr = jsonNode.get("status").asText();

            Sauce sauce = sauceRepository.findBySauceName(sauceName)
                    .orElseThrow(() -> new SauceNotFoundException(sauceName));

            fileUrl = sauce.getImg();

            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            sauce.updateSauce(
                    sauceNameFromJson,
                    calorie,
                    price,
                    fileUrl,
                    statusStr
            );

            sauceRepository.save(sauce);
            // 메시지 전송

            return toResponseDTO(sauce);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // Sauce -> SauceResponseDTO 변환 메서드
    private SauceResponseDTO toResponseDTO(Sauce sauce) {
        return new SauceResponseDTO(
                sauce.getUid(),
                sauce.getSauceName(),
                sauce.getCalorie(),
                sauce.getPrice(),
                sauce.getImg(),
                sauce.getStatus(),
                sauce.getCreatedDate(),
                sauce.getVersion()
        );
    }
}
