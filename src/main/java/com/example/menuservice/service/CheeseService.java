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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheeseService {
    private final CheeseRepository cheeseRepository;

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
    public CheeseResponseDTO addCheese(CheeseRequestDTO cheeseRequestDTO) {
        if (cheeseRepository.existsByCheeseName(cheeseRequestDTO.getCheeseName())) {
            throw new CheeseAlreadyExistsException(cheeseRequestDTO.getCheeseName());
        }

        Cheese cheese = Cheese.builder()
                .cheeseName(cheeseRequestDTO.getCheeseName())
                .calorie(cheeseRequestDTO.getCalorie())
                .price(cheeseRequestDTO.getPrice())
                .img(cheeseRequestDTO.getImg())
                .status("active")
                .build();

        return toResponseDTO(cheeseRepository.save(cheese));
    }

    // 치즈 삭제
    @Transactional
    public void removeCheese(String cheeseName) {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));
        cheeseRepository.delete(cheese);
    }

    // 치즈 수정 (변경 감지 활용)
    @Transactional
    public CheeseResponseDTO editCheeseDetails(String cheeseName, CheeseRequestDTO cheeseRequestDTO) {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));

        // ✅ 변경 감지로 자동 업데이트
        cheese.updateCheese(
                cheeseRequestDTO.getCheeseName(),
                cheeseRequestDTO.getCalorie(),
                cheeseRequestDTO.getPrice(),
                cheeseRequestDTO.getImg()
        );

        return toResponseDTO(cheese);
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
