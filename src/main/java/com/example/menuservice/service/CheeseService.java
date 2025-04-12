package com.example.menuservice.service;

import com.example.menuservice.status.CheeseStatus;
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
            if (cheeseRepository.existsByCheeseName(cheeseRequestDTO.getCheeseName())) {
                throw new CheeseAlreadyExistsException(cheeseRequestDTO.getCheeseName());
            }

            CheeseStatus status = CheeseStatus.valueOf(cheeseRequestDTO.getStatus().toUpperCase());

            if (file != null && !file.isEmpty()) {
                fileUrl = fileUploadService.uploadFile(file);
            }

            Cheese cheese = Cheese.builder()
                    .cheeseName(cheeseRequestDTO.getCheeseName())
                    .calorie(cheeseRequestDTO.getCalorie())
                    .price(cheeseRequestDTO.getPrice())
                    .status(status.name())
                    .img(fileUrl)
                    .build();

            return toResponseDTO(cheeseRepository.save(cheese));
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
    public CheeseResponseDTO editCheeseDetails(String cheeseName, CheeseRequestDTO cheeseRequestDTO, MultipartFile file) throws IOException {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));

        String fileUrl = cheese.getImg();

        try {
            if (file != null && !file.isEmpty()) {
                if (fileUrl != null) {
                    fileUploadService.deleteFile(fileUrl);
                }
                fileUrl = fileUploadService.uploadFile(file);
            }

            cheese.updateCheese(
                    cheeseRequestDTO.getCheeseName(),
                    cheeseRequestDTO.getCalorie(),
                    cheeseRequestDTO.getPrice(),
                    fileUrl,
                    cheeseRequestDTO.getStatus()
            );

            return toResponseDTO(cheese);
        } catch (Exception e) {
            if (fileUrl != null) {
                fileUploadService.deleteFile(fileUrl);
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
