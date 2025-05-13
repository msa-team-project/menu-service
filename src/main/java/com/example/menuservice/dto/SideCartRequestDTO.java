package com.example.menuservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SideCartRequestDTO {

    private Long uid;
    private Long userUid;
    private Long socialUid;
    private String sideName;
    private int amount;
    private Long totalPrice;
    private Double calorie;
    private Long unitPrice;  // ✅ 추가
}