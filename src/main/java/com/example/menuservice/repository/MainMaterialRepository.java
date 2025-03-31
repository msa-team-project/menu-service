package com.example.menuservice.repository;

import com.example.menuservice.domain.MainMaterial;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MainMaterialRepository extends CrudRepository<MainMaterial, Long> {

    // 재료 이름으로 재료를 조회하는 메서드
    Optional<MainMaterial> findByMaterialName(String materialName);

    // 재료 상태를 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE MainMaterial m SET m.status = :status WHERE m.uid = :uid")
    void updateStatusByUid(Long uid, String status);

    // 재료 이름으로 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM MainMaterial WHERE material_name = :materialName")
    void deleteByMaterialName(String materialName);

    // 재료 이름이 존재하는지 확인하는 메서드
    boolean existsByMaterialName(String materialName);
}
