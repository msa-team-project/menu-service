package com.example.menuservice.event;

import com.example.menuservice.type.EventType;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientEventDTO implements Serializable {
    private String type;            // "bread" / "cheeze"
    private Long id;        // 식별자
    private String name;
    private int price;
    private Double calorie;
    private String img;// 사용자 친화적 정보
    private String status;
    private LocalDateTime createdDate;
    private int version;

}
