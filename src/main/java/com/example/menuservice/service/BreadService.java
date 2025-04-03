package com.example.menuservice.service;

import com.example.menuservice.BreadStatus;
import com.example.menuservice.domain.Bread;
import com.example.menuservice.dto.BreadRequestDTO;
import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.exception.BreadAlreadyExistsException;
import com.example.menuservice.exception.BreadNotFoundException;
import com.example.menuservice.repository.BreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreadService {
    private final BreadRepository breadRepository;

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
    public BreadResponseDTO addBread(BreadRequestDTO breadRequestDTO) {
        if (breadRepository.existsByBreadName(breadRequestDTO.getBreadName())) {
            throw new BreadAlreadyExistsException(breadRequestDTO.getBreadName());
        }

        // ✅ 1. 상태 값 검증 (기본값: ACTIVE)
        BreadStatus status;
        try {
            status = BreadStatus.valueOf(breadRequestDTO.getStatus().toUpperCase()); // 대소문자 구분 없이 처리
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("잘못된 상태 값입니다: " + breadRequestDTO.getStatus());
        }

        // ✅ 2. 빵 저장
        Bread bread = Bread.builder()
                .breadName(breadRequestDTO.getBreadName())
                .calorie(breadRequestDTO.getCalorie())
                .price(breadRequestDTO.getPrice())
                .img(breadRequestDTO.getImg())
                .status(String.valueOf(status)) // 유효한 상태 값 저장
                .build();

        return toResponseDTO(breadRepository.save(bread));
    }


    // 빵 삭제
    @Transactional
    public void removeBread(String breadName) {
        Bread bread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));
        breadRepository.delete(bread);
    }

    // 빵 수정
    @Transactional
    public BreadResponseDTO editBreadDetails(String breadName, BreadRequestDTO breadRequestDTO) {
        Bread existingBread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));

        existingBread.updateBread(
                breadRequestDTO.getBreadName(),
                breadRequestDTO.getCalorie(),
                breadRequestDTO.getPrice(),
                breadRequestDTO.getImg()
        );

        return toResponseDTO(existingBread);
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
