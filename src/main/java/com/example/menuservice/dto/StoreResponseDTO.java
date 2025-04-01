package com.example.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Builder
public class StoreResponseDTO {
    private int    uid;
    private String  storeName;
    private String  address;
    private int    postcode;
    private String  status;
    private Instant createdDate;
}
