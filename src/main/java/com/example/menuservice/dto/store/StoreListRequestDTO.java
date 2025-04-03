package com.example.menuservice.dto.store;

import lombok.Getter;

@Getter
public class StoreListRequestDTO {
    private Long lastUid;
    private int limit;
}
