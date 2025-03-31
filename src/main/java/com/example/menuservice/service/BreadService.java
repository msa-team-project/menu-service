package com.example.menuservice.service;

import com.example.menuservice.domain.Bread;
import com.example.menuservice.dto.BreadRequestDTO;
import com.example.menuservice.dto.BreadResponseDTO;
import com.example.menuservice.exception.BreadAlreadyExistsException;
import com.example.menuservice.exception.BreadNotFoundException;
import com.example.menuservice.repository.BreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreadService {
    private final BreadRepository breadRepository;

    // 빵 목록 조회
    public List<BreadResponseDTO> viewBreadList() {
        List<BreadResponseDTO> breadResponseDTOList = new ArrayList<>();

        for (Bread bread : breadRepository.findAll()) {
            breadResponseDTOList.add(toResponseDTO(bread));
        }
        return breadResponseDTOList;
    }

    // 빵 이름으로 빵 조회
    public BreadResponseDTO viewBread(String breadName) {
        Bread bread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));
        return toResponseDTO(bread);
    }

    // 빵 추가
    public BreadResponseDTO addBread(BreadRequestDTO breadRequestDTO) {
        if (breadRepository.existsByBreadName(breadRequestDTO.getBreadName())) {
            throw new BreadAlreadyExistsException(breadRequestDTO.getBreadName());
        }

        Bread bread = Bread.builder()
                .breadName(breadRequestDTO.getBreadName())
                .calorie(breadRequestDTO.getCalorie())
                .price(breadRequestDTO.getPrice())
                .img(breadRequestDTO.getImg())
                .status("active")
                .build();

        Bread savedBread = breadRepository.save(bread);
        return toResponseDTO(savedBread);
    }

    // 빵 삭제
    public void removeBread(String breadName) {
        if (!breadRepository.existsByBreadName(breadName)) {
            throw new BreadNotFoundException(breadName);
        }
        breadRepository.deleteByBreadName(breadName);
    }

    // 빵 수정
    @Transactional
    public BreadResponseDTO editBreadDetails(String breadName, BreadRequestDTO breadRequestDTO) {
        Bread existingBread = breadRepository.findByBreadName(breadName)
                .orElseThrow(() -> new BreadNotFoundException(breadName));

        Bread updatedBread = Bread.builder()
                .uid(existingBread.uid())
                .breadName(breadRequestDTO.getBreadName())
                .calorie(breadRequestDTO.getCalorie())
                .price(breadRequestDTO.getPrice())
                .img(breadRequestDTO.getImg())
                .status(existingBread.status())
                .createdDate(existingBread.createdDate())
                .version(existingBread.version()) // 버전 증가
                .build();

        Bread savedBread = breadRepository.save(updatedBread);
        return toResponseDTO(savedBread);
    }

    // 빵 상태 업데이트
    public void updateBreadStatus(Long uid, String status) {
        if (!breadRepository.existsById(uid)) {
            throw new BreadNotFoundException("ID: " + uid);
        }
        breadRepository.updateStatusByUid(uid, status);
    }

    // Bread -> BreadResponseDTO 변환 메서드
    private BreadResponseDTO toResponseDTO(Bread bread) {
        return new BreadResponseDTO(
                bread.uid(),
                bread.breadName(),
                bread.calorie(),
                bread.price(),
                bread.img(),
                bread.status(),
                bread.createdDate(),
                bread.version()
        );
    }
}