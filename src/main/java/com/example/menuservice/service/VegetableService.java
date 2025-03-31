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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VegetableService {
    private final VegetableRepository vegetableRepository;

    // 채소 목록 조회
    public List<VegetableResponseDTO> viewVegetableList() {
        List<VegetableResponseDTO> vegetableResponseDTOList = new ArrayList<>();
        for (Vegetable vegetable : vegetableRepository.findAll()) {
            vegetableResponseDTOList.add(toResponseDTO(vegetable));
        }
        return vegetableResponseDTOList;
    }

    // 채소 이름으로 채소 조회
    public VegetableResponseDTO viewVegetable(String vegetableName) {
        Vegetable vegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));
        return toResponseDTO(vegetable);
    }

    // 채소 추가

    public VegetableResponseDTO addVegetable(VegetableRequestDTO vegetableRequestDTO) {
        if (vegetableRepository.existsByVegetableName(vegetableRequestDTO.getVegetableName())) {
            throw new VegetableAlreadyExistsException(vegetableRequestDTO.getVegetableName());
        }

        Vegetable vegetable = Vegetable.builder()
                .vegetableName(vegetableRequestDTO.getVegetableName())
                .calorie(vegetableRequestDTO.getCalorie())
                .price(vegetableRequestDTO.getPrice())
                .img(vegetableRequestDTO.getImg())
                .status("active")
                .build();

        Vegetable savedVegetable = vegetableRepository.save(vegetable);
        return toResponseDTO(savedVegetable);
    }

    // 채소 삭제
    public void removeVegetable(String vegetableName) {
        if (!vegetableRepository.existsByVegetableName(vegetableName)) {
            throw new VegetableNotFoundException(vegetableName);
        }
        vegetableRepository.deleteByVegetableName(vegetableName);
    }

    // 채소 수정
    @Transactional
    public VegetableResponseDTO editVegetableDetails(String vegetableName, VegetableRequestDTO vegetableRequestDTO) {
        Vegetable existingVegetable = vegetableRepository.findByVegetableName(vegetableName)
                .orElseThrow(() -> new VegetableNotFoundException(vegetableName));

        Vegetable updatedVegetable = Vegetable.builder()
                .uid(existingVegetable.uid())
                .vegetableName(vegetableRequestDTO.getVegetableName())
                .calorie(vegetableRequestDTO.getCalorie())
                .price(vegetableRequestDTO.getPrice())
                .img(vegetableRequestDTO.getImg())
                .status(existingVegetable.status())
                .createdDate(existingVegetable.createdDate())
                .version(existingVegetable.version() ) // 버전 증가
                .build();

        Vegetable savedVegetable = vegetableRepository.save(updatedVegetable);
        return toResponseDTO(savedVegetable);
    }

    // 채소 상태 업데이트
    public void updateVegetableStatus(Long uid, String status) {
        if (!vegetableRepository.existsById(uid)) {
            throw new VegetableNotFoundException("ID: " + uid);
        }
        vegetableRepository.updateStatusByUid(uid, status);
    }

    // Vegetable -> VegetableResponseDTO 변환 메서드
    private VegetableResponseDTO toResponseDTO(Vegetable vegetable) {
        return new VegetableResponseDTO(
                vegetable.uid(),
                vegetable.vegetableName(),
                vegetable.calorie(),
                vegetable.price(),
                vegetable.img(),
                vegetable.status(),
                vegetable.createdDate(),
                vegetable.version()
        );
    }
}
