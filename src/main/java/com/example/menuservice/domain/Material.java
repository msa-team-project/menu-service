package com.example.menuservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Material {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL AUTO_INCREMENT 설정
        private Long uid;

        @NotBlank(message = "The material name must be defined.")
        @Column(nullable = false)
        private String materialName;

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

        public Material(long l, String lettuce) {
        }


        // ✅ 재료 정보 업데이트 메서드 추가
        public void updateMaterial(String materialName, Double calorie, int price, String img,String status) {
                this.materialName = materialName;
                this.calorie = calorie;
                this.price = price;
                this.img = img;
                this.status = status;
        }
}
