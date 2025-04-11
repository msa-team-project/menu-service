package com.example.menuservice.service;

import com.example.menuservice.status.SauceStatus;
import com.example.menuservice.domain.Sauce;
import com.example.menuservice.dto.SauceRequestDTO;
import com.example.menuservice.dto.SauceResponseDTO;
import com.example.menuservice.exception.SauceAlreadyExistsException;
import com.example.menuservice.exception.SauceNotFoundException;
import com.example.menuservice.repository.SauceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public SauceResponseDTO addSauce(SauceRequestDTO sauceRequestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            if (sauceRepository.existsBySauceName(sauceRequestDTO.getSauceName())) {
                throw new SauceAlreadyExistsException(sauceRequestDTO.getSauceName());
            }

            SauceStatus status = SauceStatus.valueOf(sauceRequestDTO.getStatus().toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Sauce sauce = Sauce.builder()
                    .sauceName(sauceRequestDTO.getSauceName())
                    .calorie(sauceRequestDTO.getCalorie())
                    .price(sauceRequestDTO.getPrice())
                    .status(status.name())
                    .img(fileUrl)
                    .build();

            return toResponseDTO(sauceRepository.save(sauce));
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 소스 삭제
    @Transactional
    public void removeSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));
        sauce.setStatus(SauceStatus.DELETED.name());
        sauceRepository.save(sauce);
    }

    // 소스 수정
    @Transactional
    public SauceResponseDTO editSauceDetails(String sauceName, SauceRequestDTO sauceRequestDTO, MultipartFile file) throws IOException {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));

        String fileUrl = sauce.getImg();
        try {
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            sauce.updateSauce(
                    sauceRequestDTO.getSauceName(),
                    sauceRequestDTO.getCalorie(),
                    sauceRequestDTO.getPrice(),
                    fileUrl,
                    sauceRequestDTO.getStatus()
            );

            return toResponseDTO(sauce);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 소스 상태 업데이트
    @Transactional
    public void updateSauceStatus(Long uid, String status) {
        Sauce sauce = sauceRepository.findById(uid)
                .orElseThrow(() -> new SauceNotFoundException("ID: " + uid));
        sauce.setStatus(status);
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
