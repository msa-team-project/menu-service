package com.example.menuservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    // 로그인 사용자
    @Column(name = "user_uid")
    private Long userUid;

    // 소셜 로그인 사용자
    @Column(name = "social_uid")
    private Long socialUid;


    // 어떤 메뉴를 담았는지 외래키로 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    // 커스텀 카트 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_cart_id")
    private CustomCart customCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "side_id")
    private Side side;


    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Double calorie;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;


    private String img;



    @Version
    private int version;
}
