package com.example.menuservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialResponseDTO {

    private Long uid;
    private String materialName;
    private Double calorie;
    private int price;
    private String img;
    private String status;
    private LocalDateTime createdDate;
    private int version;


}
