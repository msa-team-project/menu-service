package com.example.menuservice.dto.store;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreInfoResponseDTO {
    private Long uid;
    private String storeName;
    private String storeAddress;
    private String storePostcode;
    private String storeStatus;


}
