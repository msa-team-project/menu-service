package com.example.menuservice.repository;

import com.example.menuservice.domain.Side;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SideRepository extends JpaRepository<Side, Long> {

    // ✅ 사이드 이름으로 조회
    Optional<Side> findBySideName(String sideName);


    // ✅ 사이드 이름 존재 여부 확인
    boolean existsBySideName(String sideName);
}
