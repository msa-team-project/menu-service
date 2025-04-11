package com.example.menuservice.service;

import com.example.menuservice.status.BreadStatus;
import com.example.menuservice.domain.Bread;
import com.example.menuservice.dto.BreadRequestDTO;
import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.exception.BreadAlreadyExistsException;
import com.example.menuservice.exception.BreadNotFoundException;
import com.example.menuservice.repository.BreadRepository;
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
    public BreadResponseDTO addBread(BreadRequestDTO breadRequestDTO, MultipartFile file) throws IOException {
        String fileUrl = null;

        try {
            if (breadRepository.existsByBreadName(breadRequestDTO.getBreadName())) {
                throw new BreadAlreadyExistsException(breadRequestDTO.getBreadName());
            }

            BreadStatus status = BreadStatus.valueOf(breadRequestDTO.getStatus().toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Bread bread = Bread.builder()
                    .breadName(breadRequestDTO.getBreadName())
                    .calorie(breadRequestDTO.getCalorie())
                    .price(breadRequestDTO.getPrice())
                    .status(status.name())
                    .img(fileUrl)
                    .build();

            return toResponseDTO(breadRepository.save(bread));
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

    // 빵 수정
    @Transactional
    public BreadResponseDTO editBreadDetails(String breadName, BreadRequestDTO breadRequestDTO, MultipartFile file) throws IOException {
        Bread bread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));

        String fileUrl = bread.getImg();
        try {
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            bread.updateBread(
                    breadRequestDTO.getBreadName(),
                    breadRequestDTO.getCalorie(),
                    breadRequestDTO.getPrice(),
                    fileUrl,
                    breadRequestDTO.getStatus()
            );

            return toResponseDTO(bread);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
            }
            throw e;
        }
    }

    // 빵 상태 업데이트
    @Transactional
    public void updateBreadStatus(Long uid, String status) {
        Bread bread = breadRepository.findById(uid)
                .orElseThrow(() -> new BreadNotFoundException("ID: " + uid));
        bread.setStatus(status);
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
