package com.example.menuservice.repository;

import com.example.menuservice.domain.Sauce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SauceRepository extends JpaRepository<Sauce, Long> {

    // ✅ 소스 이름으로 조회
    Optional<Sauce> findBySauceName(String sauceName);



    // ✅ 소스 이름 존재 여부 확인
    boolean existsBySauceName(String sauceName);
}
