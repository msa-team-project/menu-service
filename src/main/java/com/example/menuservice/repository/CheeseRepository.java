package com.example.menuservice.repository;

import com.example.menuservice.domain.Cheese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CheeseRepository extends JpaRepository<Cheese, Long> {

    // ✅ 치즈 이름으로 조회
    Optional<Cheese> findByCheeseName(String cheeseName);



    // ✅ 치즈 이름 존재 여부 확인
    boolean existsByCheeseName(String cheeseName);
}
