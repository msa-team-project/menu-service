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
    @JoinColumn(name = "bread", nullable = false)
    private Bread bread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material1", nullable = false)
    private Material material1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material2")
    private Material material2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material3")
    private Material material3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cheese")
    private Cheese cheese;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable1", nullable = false)
    private Vegetable vegetable1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable2")
    private Vegetable vegetable2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable3")
    private Vegetable vegetable3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable4")
    private Vegetable vegetable4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable5")
    private Vegetable vegetable5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable6")
    private Vegetable vegetable6;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable7")
    private Vegetable vegetable7;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vegetable8")
    private Vegetable vegetable8;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sauce1", nullable = false)
    private Sauce sauce1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sauce2")
    private Sauce sauce2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sauce3")
    private Sauce sauce3;


    @Version
    private int version;





    }

