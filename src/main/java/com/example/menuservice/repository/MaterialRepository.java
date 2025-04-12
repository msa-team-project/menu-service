package com.example.menuservice.repository;

import com.example.menuservice.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import feign.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    // ✅ 재료 이름으로 조회
    Optional<Material> findByMaterialName(String materialName);

    // ✅ 재료 상태로 조회 (status는 String)
    @Query("SELECT m FROM Material m WHERE m.status = :status")
    List<Material> findByStatus(@Param("status") String status);

    // ✅ 재료 이름 존재 여부 확인
    boolean existsByMaterialName(String materialName);
}
