package com.example.menuservice.dto;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomCartRequestDTO {

    @NotNull(message = "The price must be defined.")
    @Positive(message = "The price must be greater than zero.")
    private Long price;

    @NotNull(message = "The calorie count must be defined.")
    private Double calorie;

    @NotNull(message = "The bread must be defined.")
    private Long bread;

    @NotNull(message = "The main material 1 must be defined.")
    private Long material1;

    private Long material2;

    private Long material3;
    @NotNull(message = "The cheese must be defined.")
    private Long cheese;

    @NotNull(message = "At least one vegetable must be defined.")
    private Long vegetable1;

    private Long vegetable2;
    private Long vegetable3;
    private Long vegetable4;
    private Long vegetable5;
    private Long vegetable6;
    private Long vegetable7;
    private Long vegetable8;

    @NotNull(message = "At least one sauce must be defined.")
    private Long sauce1;

    private Long sauce2;
    private Long sauce3;
    private Long userUid;
    private Long socialUid;




}
