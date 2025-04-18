package com.example.menuservice.dto;



import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomCartRequestDTO {

    @NotNull(message = "The bread must be defined.")
    private Long bread;

    @NotNull(message = "The main material 1 must be defined.")
    private Long material1;

    private Long material2;
    private Long material3;

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





}
