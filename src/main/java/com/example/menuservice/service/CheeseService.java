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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheeseService {
    private final CheeseRepository cheeseRepository;

    // 치즈 목록 조회
    public List<CheeseResponseDTO> viewCheeseList() {
        List<CheeseResponseDTO> cheeseResponseDTOList = new ArrayList<>();
        for (Cheese cheese : cheeseRepository.findAll()) {
            cheeseResponseDTOList.add(toResponseDTO(cheese));
        }
        return cheeseResponseDTOList;
    }

    // 치즈 이름으로 조회
    public CheeseResponseDTO viewCheese(String cheeseName) {
        Cheese cheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));
        return toResponseDTO(cheese);
    }

    // 치즈 추가
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

        Cheese savedCheese = cheeseRepository.save(cheese);
        return toResponseDTO(savedCheese);
    }

    // 치즈 삭제
    public void removeCheese(String cheeseName) {
        if (!cheeseRepository.existsByCheeseName(cheeseName)) {
            throw new CheeseNotFoundException(cheeseName);
        }
        cheeseRepository.deleteByCheeseName(cheeseName);
    }

    // 치즈 수정
    @Transactional
    public CheeseResponseDTO editCheeseDetails(String cheeseName, CheeseRequestDTO cheeseRequestDTO) {
        Cheese existingCheese = cheeseRepository.findByCheeseName(cheeseName)
                .orElseThrow(() -> new CheeseNotFoundException(cheeseName));

        Cheese updatedCheese = Cheese.builder()
                .uid(existingCheese.uid())
                .cheeseName(cheeseRequestDTO.getCheeseName())
                .calorie(cheeseRequestDTO.getCalorie())
                .price(cheeseRequestDTO.getPrice())
                .img(cheeseRequestDTO.getImg())
                .status(existingCheese.status())
                .createdDate(existingCheese.createdDate())
                .version(existingCheese.version()) // 버전 증가
                .build();

        Cheese savedCheese = cheeseRepository.save(updatedCheese);
        return toResponseDTO(savedCheese);
    }

    // 치즈 상태 업데이트
    public void updateCheeseStatus(Long uid, String status) {
        if (!cheeseRepository.existsById(uid)) {
            throw new CheeseNotFoundException("ID: " + uid);
        }
        cheeseRepository.updateStatusByUid(uid, status);
    }

    // Cheese -> CheeseResponseDTO 변환 메서드
    private CheeseResponseDTO toResponseDTO(Cheese cheese) {
        return new CheeseResponseDTO(
                cheese.uid(),
                cheese.cheeseName(),
                cheese.calorie(),
                cheese.price(),
                cheese.img(),
                cheese.status(),
                cheese.createdDate(),
                cheese.version()
        );
    }
}
