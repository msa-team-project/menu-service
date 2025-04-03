package com.example.menuservice.repository;

import com.example.menuservice.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    // ✅ 재료 이름으로 조회
    Optional<Material> findByMaterialName(String materialName);



    // ✅ 재료 이름 존재 여부 확인
    boolean existsByMaterialName(String materialName);
}
