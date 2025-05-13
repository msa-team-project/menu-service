package com.example.menuservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "vegetable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Vegetable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT
        private Long uid;

        @NotBlank(message = "The vegetable name must be defined.")
        @Column(nullable = false)
        private String vegetableName;

        @NotNull(message = "The calorie count must be defined.")
        @Column(nullable = false)
        private Double calorie;

        @NotNull(message = "The price must be defined.")
        @Positive(message = "The price must be greater than zero.")
        @Column(nullable = false)
        private int price;

        @NotBlank(message = "The image URL must be defined.")
        @Column(nullable = false)
        private String img;

        @Column(nullable = false)
        private String status;

        @Column(name = "created_date", updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        private LocalDateTime createdDate;

        @Version
        private int version;

        public Vegetable(String carrot, int i, int i1, String name, String image) {
        }


        // ✅ 채소 정보 업데이트 메서드 추가
        public void updateVegetable(String vegetableName, double calorie, int price, String img,String status) {
                this.vegetableName = vegetableName;
                this.calorie = calorie;
                this.price = price;
                this.img = img;
                this.status = status;
        }
}
