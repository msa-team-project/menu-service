//package com.example.menuservice.domain;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import lombok.Builder;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.Version;
//import org.springframework.data.relational.core.mapping.Column;
//
//import java.time.Instant;
//
//@Builder
//public record Store(
//        @Id
//        int uid,
//
//        @NotBlank(message = "the store name must be defined.")
//        String storeName,
//
//        @NotBlank(message = "the address must be defined.")
//        String address,
//
//        @NotNull(message = "the postcode must be defined.")
//        int postcode,
//
//        @NotBlank(message ="the status must be defined." )
//        String status,
//
//        @Column("created_date")
//        @CreatedDate
//        Instant createdDate,
//
//        @Version
//        int version
//
//) {
//}
