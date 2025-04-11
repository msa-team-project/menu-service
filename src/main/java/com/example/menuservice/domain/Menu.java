package com.example.menuservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Menu {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long uid;

        @NotBlank(message = "The menu name must be defined.")
        @Column(nullable = false)
        private String menuName;

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
        @JoinColumn(name = "material2_id" ,nullable = true)
        private Material material2;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "material3_id",nullable = true)
        private Material material3;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cheese_id",nullable = false)
        private Cheese cheese;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable1_id", nullable = false)
        private Vegetable vegetable1;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable2_id",nullable = true)
        private Vegetable vegetable2;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable3_id",nullable = true)
        private Vegetable vegetable3;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable4_id",nullable = true)
        private Vegetable vegetable4;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable5_id",nullable = true)
        private Vegetable vegetable5;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable6_id",nullable = true)
        private Vegetable vegetable6;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable7_id",nullable = true)
        private Vegetable vegetable7;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "vegetable8_id",nullable = true)
        private Vegetable vegetable8;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "sauce1_id", nullable = false)
        private Sauce sauce1;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "sauce2_id",nullable = true)
        private Sauce sauce2;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "sauce3_id",nullable = true)
        private Sauce sauce3;

        @NotBlank(message = "The image URL must be defined.")
        @Column(nullable = false)
        private String img;

        @Column(nullable = false)
        private String status;

        @Column(name = "created_date", updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        private Instant createdDate;

        @Version
        private int version;

        public void updateMenu(String menuName, Long price, double calorie, Bread bread,
                               Material material1, Material material2, Material material3,
                               Cheese cheese,
                               Vegetable vegetable1, Vegetable vegetable2, Vegetable vegetable3,
                               Vegetable vegetable4, Vegetable vegetable5, Vegetable vegetable6,
                               Vegetable vegetable7, Vegetable vegetable8,
                               Sauce sauce1, Sauce sauce2, Sauce sauce3,
                               String img, String status) {

                this.menuName = menuName;
                this.price = price;
                this.calorie = calorie;
                this.bread = bread;
                this.material1 = material1;
                this.material2 = material2;
                this.material3 = material3;
                this.cheese = cheese;
                this.vegetable1 = vegetable1;
                this.vegetable2 = vegetable2;
                this.vegetable3 = vegetable3;
                this.vegetable4 = vegetable4;
                this.vegetable5 = vegetable5;
                this.vegetable6 = vegetable6;
                this.vegetable7 = vegetable7;
                this.vegetable8 = vegetable8;
                this.sauce1 = sauce1;
                this.sauce2 = sauce2;
                this.sauce3 = sauce3;
                this.img = img;
                this.status = status;
        }
}
