package com.example.menuservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VegetableRequestDTO {

    @NotBlank(message = "The vegetable name must be defined.")
    private String vegetableName;

    @NotNull(message = "The calorie count must be defined.")
    private Double calorie;

    @NotNull(message = "The price must be defined.")
    @Positive(message = "The price must be greater than zero.")
    private int price;


    private String img;

    private String status;

    // 이미지 파일과 URL을 함께 관리
    private MultipartFile file; // 업로드할 이미지 파일

    public VegetableRequestDTO(String carrot, int i, int i1, String active) {
    }
}
