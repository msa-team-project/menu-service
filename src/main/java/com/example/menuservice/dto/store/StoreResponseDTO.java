package com.example.menuservice.dto.store;

import com.example.menuservice.domain.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
public class StoreResponseDTO {
    private Long    uid;
    private String  storeName;
    private String  storeAddress;
    private int     storePostcode;
    private String  storeStatus;
    private Instant storeCreatedDate;

//    public StoreResponseDTO(Store store) {
//        this.uid = store.getUid();
//        this.storeName = store.getStoreName();
//        this.storeAddress = store.getStoreAddress();
//        this.storePostcode = store.getStorePostcode();
//        this.storeStatus = store.getStoreStatus();
//        this.storeCreatedDate = store.getStoreCreatedDate();
//
//    }



}