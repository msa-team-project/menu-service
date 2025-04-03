package com.example.menuservice.repository;

import com.example.menuservice.domain.Bread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BreadRepository extends JpaRepository<Bread, Long> {

    // ✅ 빵 이름으로 조회
    Optional<Bread> findByBreadName(String breadName);


    // ✅ 빵 이름 존재 여부 확인
    boolean existsByBreadName(String breadName);
}
