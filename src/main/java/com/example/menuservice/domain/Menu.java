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
        @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT 설정
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

        @NotNull(message = "The bread must be defined.")
        @Column(nullable = false)
        private Long bread;

        @NotNull(message = "The material 1 must be defined.")
        @Column(nullable = false)
        private Long material1;

        private Long material2;
        private Long material3;

        private Long cheese;

        @NotNull(message = "At least one vegetable must be defined.")
        @Column(nullable = false)
        private Long vegetable1;

        private Long vegetable2;
        private Long vegetable3;
        private Long vegetable4;
        private Long vegetable5;
        private Long vegetable6;
        private Long vegetable7;
        private Long vegetable8;

        @NotNull(message = "At least one sauce must be defined.")
        @Column(nullable = false)
        private Long sauce1;

        private Long sauce2;
        private Long sauce3;

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
        // ✅ 메뉴 정보 업데이트 메서드 추가
        public void updateMenu(String menuName, Long price, double calorie, Long bread,
                               Long material1, Long material2, Long material3, Long cheese,
                               Long vegetable1, Long vegetable2, Long vegetable3, Long vegetable4,
                               Long vegetable5, Long vegetable6, Long vegetable7, Long vegetable8,
                               Long sauce1, Long sauce2, Long sauce3, String img) {
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
        }
}
