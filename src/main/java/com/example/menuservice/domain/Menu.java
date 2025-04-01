package com.example.menuservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;



@Builder
@Table("menu")
public record Menu(
        @Id
        Long uid,

        @NotBlank(message = "The menu name must be defined.")
        String menuName,

        @NotNull(message = "The price must be defined.")
        @Positive(message = "The price must be greater than zero.")
        Long price,

        @NotNull(message = "The calorie count must be defined.")
        Double calorie,

        @NotNull(message = "The bread must be defined.")
        Long bread,


        @NotNull(message = "The material 1 must be defined.")
        Long material1,

        Long material2,
        Long material3,

        Long cheese,

        @NotNull(message = "At least one vegetable must be defined.")
        Long vegetable1,

        Long vegetable2,
        Long vegetable3,
        Long vegetable4,
        Long vegetable5,
        Long vegetable6,
        Long vegetable7,
        Long vegetable8,

        @NotNull(message = "At least one sauce must be defined.")
        Long sauce1,

        Long sauce2,
        Long sauce3,

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
