package com.example.menuservice.repository;

import com.example.menuservice.domain.Vegetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VegetableRepository extends JpaRepository<Vegetable, Long> {

    // ✅ 채소 이름으로 조회
    Optional<Vegetable> findByVegetableName(String vegetableName);


    // ✅ 채소 이름 존재 여부 확인
    boolean existsByVegetableName(String vegetableName);
}
