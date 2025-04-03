package com.example.menuservice.service;

import com.example.menuservice.domain.Vegetable;
import com.example.menuservice.dto.VegetableRequestDTO;
import com.example.menuservice.dto.VegetableResponseDTO;
import com.example.menuservice.exception.VegetableAlreadyExistsException;
import com.example.menuservice.exception.VegetableNotFoundException;
import com.example.menuservice.repository.VegetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VegetableService {
    private final VegetableRepository vegetableRepository;

    // 채소 목록 조회
    public List<VegetableResponseDTO> viewVegetableList() {
        return vegetableRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // 채소 이름으로 조회
    public VegetableResponseDTO viewVegetable(String vegetableName) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));
        return toResponseDTO(vegetable);
    }

    // 채소 추가
    @Transactional
    public VegetableResponseDTO addVegetable(VegetableRequestDTO requestDTO) {
        if (vegetableRepository.existsByVegetableName(requestDTO.getVegetableName())) {
            throw new VegetableAlreadyExistsException(requestDTO.getVegetableName());
        }

        Vegetable vegetable = Vegetable.builder()
                .vegetableName(requestDTO.getVegetableName())
                .calorie(requestDTO.getCalorie())
                .price(requestDTO.getPrice())
                .img(requestDTO.getImg())
                .status("active")
                .build();

        return toResponseDTO(vegetableRepository.save(vegetable));
    }

    // 채소 삭제
    @Transactional
    public void removeVegetable(String vegetableName) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));
        vegetableRepository.delete(vegetable);
    }

    // 채소 수정 (변경 감지 활용)
    @Transactional
    public VegetableResponseDTO editVegetableDetails(String vegetableName, VegetableRequestDTO requestDTO) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));

        // ✅ 변경 감지를 활용한 업데이트
        vegetable.updateVegetable(
                requestDTO.getVegetableName(),
                requestDTO.getCalorie(),
                requestDTO.getPrice(),
                requestDTO.getImg()
        );

        return toResponseDTO(vegetable);
    }

    // 채소 상태 업데이트
    @Transactional
    public void updateVegetableStatus(Long uid, String status) {
        Vegetable vegetable = vegetableRepository.findById(uid)
                .orElseThrow(() -> new VegetableNotFoundException("ID: " + uid));
        vegetable.setStatus(status);
    }

    // Vegetable -> VegetableResponseDTO 변환 메서드
    private VegetableResponseDTO toResponseDTO(Vegetable vegetable) {
        return new VegetableResponseDTO(
                vegetable.getUid(),
                vegetable.getVegetableName(),
                vegetable.getCalorie(),
                vegetable.getPrice(),
                vegetable.getImg(),
                vegetable.getStatus(),
                vegetable.getCreatedDate(),
                vegetable.getVersion()
        );
    }
}
