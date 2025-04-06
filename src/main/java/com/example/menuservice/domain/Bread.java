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
@Table(name = "bread")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Bread {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL 자동 증가(AUTO_INCREMENT)
        private Long uid;

        @NotBlank(message = "The bread name must be defined.")
        @Column(nullable = false)
        private String breadName;

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
        private Instant createdDate;

        @Version
        private int version;

        // ✅ 빵 정보 업데이트 메서드 추가
        public void updateBread(String breadName, Double calorie, int price, String img,String status) {
                this.breadName = breadName;
                this.calorie = calorie;
                this.price = price;
                this.img = img;
                this.status = status;

        }

}
