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
public class SideRequestDTO {

    @NotBlank(message = "The side name must be defined.")
    private String sideName;

    @NotNull(message = "The calorie count must be defined.")
    private Double calorie;

    @NotNull(message = "The price must be defined.")
    @Positive(message = "The price must be greater than zero.")
    private int price;


    private String img;

    private String status;



}
