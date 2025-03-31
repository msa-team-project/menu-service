package com.example.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDTO {

    private Long uid;
    private String menuName;
    private Long price;
    private Double calorie;
    private Long bread;
    private Long mainMaterial1;
    private Long mainMaterial2;
    private Long mainMaterial3;
    private Long cheese;
    private Long vegetable1;
    private Long vegetable2;
    private Long vegetable3;
    private Long vegetable4;
    private Long vegetable5;
    private Long vegetable6;
    private Long vegetable7;
    private Long vegetable8;
    private Long sauce1;
    private Long sauce2;
    private Long sauce3;
    private String img;
    private String status;
    private Instant createdDate;
    private int version;
}
