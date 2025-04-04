package com.example.menuservice.service;

import com.example.menuservice.domain.Cheese;
import com.example.menuservice.dto.CheeseRequestDTO;
import com.example.menuservice.dto.CheeseResponseDTO;
import com.example.menuservice.exception.CheeseAlreadyExistsException;
import com.example.menuservice.exception.CheeseNotFoundException;
import com.example.menuservice.repository.CheeseRepository;
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
    public CheeseResponseDTO addCheese(CheeseRequestDTO cheeseRequestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            // ✅ 1. 중복 체크
            if (cheeseRepository.existsByCheeseName(cheeseRequestDTO.getCheeseName())) {
                throw new CheeseAlreadyExistsException(cheeseRequestDTO.getCheeseName());
            }

            // ✅ 2. S3에 이미지 업로드 (DB 저장 전에 수행)
            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 3. 치즈 정보 저장
            Cheese cheese = Cheese.builder()
                    .cheeseName(cheeseRequestDTO.getCheeseName())
                    .calorie(cheeseRequestDTO.getCalorie())
                    .price(cheeseRequestDTO.getPrice())
                    .img(fileUrl)
                    .status("active")
                    .build();

            return toResponseDTO(cheeseRepository.save(cheese));

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

    // 치즈 삭제
    @Transactional
    public void removeCheese(String cheeseName) {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));

        // ✅ 상태를 "DELETED"로 변경
        cheese.setStatus("DELETED");

        // ✅ 변경된 상태 저장
        cheeseRepository.save(cheese);
    }

    // 치즈 수정
    @Transactional
    public CheeseResponseDTO editCheeseDetails(String cheeseName, CheeseRequestDTO cheeseRequestDTO, MultipartFile file) throws IOException {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));

        String fileUrl = cheese.getImg();

        try {
            // ✅ 새 이미지 업로드 시 기존 이미지 삭제
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            // ✅ 변경 감지로 자동 업데이트
            cheese.updateCheese(
                    cheeseRequestDTO.getCheeseName(),
                    cheeseRequestDTO.getCalorie(),
                    cheeseRequestDTO.getPrice(),
                    fileUrl // 새 이미지 URL 반영
            );

            return toResponseDTO(cheese);
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

    // 치즈 상태 업데이트
    @Transactional
    public void updateCheeseStatus(Long uid, String status) {
        Cheese cheese = cheeseRepository.findById(uid)
                .orElseThrow(() -> new CheeseNotFoundException("ID: " + uid));
        cheese.setStatus(status);
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
