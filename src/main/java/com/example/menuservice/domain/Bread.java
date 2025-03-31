package com.example.menuservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import java.time.Instant;

@Builder
public record Bread(
        @Id
        Long uid,

        @NotBlank(message = "The bread name must be defined.")
        String breadName,

        @NotNull(message = "The calorie count must be defined.")
        Double calorie,

        @NotNull(message = "The price must be defined.")
        @Positive(message = "The price must be greater than zero.")
        int price,

        @NotBlank(message = "The image URL must be defined.")
        String img,

        @Column("status")
        String status,

        @Column("created_date")
        @CreatedDate
        Instant createdDate,

        @Version
        int version
) {}
