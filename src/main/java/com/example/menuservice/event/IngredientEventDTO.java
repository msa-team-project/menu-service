package com.example.menuservice.event;

import com.example.menuservice.type.EventType;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientEventDTO implements Serializable {
    private String type;            // "bread" / "cheeze"
    private Long id;        // 식별자
    private String name;
    private String img;// 사용자 친화적 정보
    private String status;      // ACTIVE, INACTIVE 등
}
