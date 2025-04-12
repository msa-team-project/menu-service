package com.example.menuservice.repository;

import com.example.menuservice.domain.Side;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import feign.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface SideRepository extends JpaRepository<Side, Long> {

    // ✅ 사이드 이름으로 조회
    Optional<Side> findBySideName(String sideName);

    // ✅ 사이드 상태로 조회 (status는 String)
    @Query("SELECT s FROM Side s WHERE s.status = :status")
    List<Side> findByStatus(@Param("status") String status);

    // ✅ 사이드 이름 존재 여부 확인
    boolean existsBySideName(String sideName);
}
