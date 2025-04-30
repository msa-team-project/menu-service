package com.example.menuservice.event;

import com.example.menuservice.type.EventType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuEventDTO {

    private Long menuId;        // 메뉴 식별자
    private String menuName;    // 사용자 친화적 정보
    private String status;      // ACTIVE, INACTIVE 등
    private EventType eventType;   // created, updated, deleted 등
    private Instant updatedAt;  // 언제 이벤트가 발생했는지
}
