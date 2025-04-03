package com.example.menuservice.domain;

import com.example.menuservice.dto.store.StoreResponseDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "store")
public class Store{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long uid;

        @NotBlank(message = "the store name must be defined.")
        @Column(name = "store_name")
        private String storeName;

        @NotBlank(message = "the address must be defined.")
        @Column(name = "address")
        private String storeAddress;

        @NotNull(message = "the postcode must be defined.")
        @Column(name = "postcode")
        private int storePostcode;

        @NotBlank(message = "the status must be defined.")
        @Column(name = "status")
        private String storeStatus;

        @CreatedDate
        @Column(name = "created_date", updatable = false)
        private Instant storeCreatedDate;

        @Version
        private int version;

        // Store -> StoreResponseDTO 변환 메서드
        public StoreResponseDTO toStoreResponseDTO () {
                return StoreResponseDTO.builder()
                        .uid(this.getUid())
                        .storeName(this.getStoreName())
                        .storeAddress(this.getStoreAddress())
                        .storePostcode(this.getStorePostcode())
                        .storeStatus(this.getStoreStatus())
                        .storeCreatedDate(this.getStoreCreatedDate())
                        .build();

        }
}