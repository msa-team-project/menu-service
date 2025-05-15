package com.example.menuservice.event;

import com.example.menuservice.type.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuEventDTO implements Serializable {

    private Long menuId;        // 메뉴 식별자
    private String menuName;
    private Long price;
    private Double calorie;
    private Long bread;
    private Long material1;

    private Long material2;
    private Long material3;

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
          // ACTIVE, INACTIVE 등
    private EventType eventType;   // created, updated, deleted 등
    private LocalDateTime updatedAt;  // 언제 이벤트가 발생했는지

}
