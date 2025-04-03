package com.example.menuservice.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class StoreRequestDTO {
    private String storeName;
    private String storeAddress;
    private int storePostcode;
    private String storeStatus;
}
