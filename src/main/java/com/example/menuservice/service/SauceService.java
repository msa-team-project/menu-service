package com.example.menuservice.service;

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

    // 소스 이름으로 조회
    public SauceResponseDTO viewSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));
        return toResponseDTO(sauce);
    }

    // 소스 추가
    @Transactional
    public SauceResponseDTO addSauce(SauceRequestDTO requestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ✅ 1. 중복 체크
            if (sauceRepository.existsBySauceName(requestDTO.getSauceName())) {
                throw new SauceAlreadyExistsException(requestDTO.getSauceName());
            }

            // ✅ 2. S3에 이미지 업로드 (DB 저장 전에 수행)
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 3. 소스 정보 저장
            Sauce sauce = Sauce.builder()
                    .sauceName(requestDTO.getSauceName())
                    .calorie(requestDTO.getCalorie())
                    .price(requestDTO.getPrice())
                    .img(fileUrl)
                    .status("active")
                    .build();

            return toResponseDTO(sauceRepository.save(sauce));

        } catch (Exception e) {
            // 🚨 트랜잭션 롤백 전에 업로드된 이미지 삭제
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                    System.out.println("🚨 저장 실패로 인해 S3 파일 삭제 완료: " + fileUrl);
                } catch (Exception s3Exception) {
                    System.out.println("⚠ S3 파일 삭제 실패: " + fileUrl);
                }
            }

            throw e; // 예외 다시 던져서 트랜잭션 롤백
        }
    }

    // 소스 삭제 (상태를 "DELETED"로 변경)
    @Transactional
    public void removeSauce(String sauceName) {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));

        // ✅ 상태를 "DELETED"로 변경
        sauce.setStatus("DELETED");

        // ✅ 변경된 상태 저장
        sauceRepository.save(sauce);
    }

    // 소스 수정
    @Transactional
    public SauceResponseDTO editSauceDetails(String sauceName, SauceRequestDTO requestDTO, MultipartFile file) throws IOException {
        Sauce sauce = sauceRepository.findBySauceName(sauceName)
                .orElseThrow(() -> new SauceNotFoundException(sauceName));

        String fileUrl = sauce.getImg();

        try {
            // ✅ 새 이미지 업로드 시 기존 이미지 삭제
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 변경 감지로 자동 업데이트
            sauce.updateSauce(
                    requestDTO.getSauceName(),
                    requestDTO.getCalorie(),
                    requestDTO.getPrice(),
                    fileUrl // 새 이미지 URL 반영
            );

            return toResponseDTO(sauce);
        } catch (Exception e) {
            // 🚨 트랜잭션 롤백 전에 업로드된 새 이미지 삭제
            if (fileUrl != null) {
                try {
                    fileUploadService.deleteFile(fileUrl);
                } catch (Exception s3Exception) {
                    System.out.println("⚠ S3 파일 삭제 실패: " + fileUrl);
                }
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
                sauce.getStatus(),
                sauce.getImg(),
                sauce.getCreatedDate(),
                sauce.getVersion()
        );
    }
}
