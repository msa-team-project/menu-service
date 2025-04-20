package com.example.menuservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "custom_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @NotNull(message = "The price must be defined.")
    @Positive(message = "The price must be greater than zero.")
    @Column(nullable = false)
    private Long price;

    @NotNull(message = "The calorie count must be defined.")
    @Column(nullable = false)
    private Double calorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bread_id", nullable = false)
    private Bread bread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material1_id", nullable = false)
    private Material material1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material2_id")
    private Material material2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material3_id")
    private Material material3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cheese_id")
    private Cheese cheese;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable1_id", nullable = false)
    private Vegetable vegetable1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable2_id")
    private Vegetable vegetable2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable3_id")
    private Vegetable vegetable3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable4_id")
    private Vegetable vegetable4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable5_id")
    private Vegetable vegetable5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable6_id")
    private Vegetable vegetable6;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable7_id")
    private Vegetable vegetable7;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable8_id")
    private Vegetable vegetable8;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sauce1_id", nullable = false)
    private Sauce sauce1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sauce2_id")
    private Sauce sauce2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sauce3_id")
    private Sauce sauce3;

    @Version
    private int version;


    }

