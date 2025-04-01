package com.example.menuservice.dto;

import lombok.Getter;

@Getter
public class StoreRequestDTO {
    private String storeName;
    private String address;
    private int postcode;
    private String status;
}
