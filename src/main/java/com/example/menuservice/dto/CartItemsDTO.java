package com.example.menuservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemsDTO {

    private Long uid;
    private Long userUid;
    private Long socialUid;
    private String menuName;
    private int amount;
    private Long totalPrice;
    private Double calorie;
    private Long unitPrice;  // ✅ 추가
    private String img;
    private Long customCartUid;
}


