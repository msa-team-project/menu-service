package com.example.menuservice.repository;

import com.example.menuservice.domain.Vegetable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface VegetableRepository extends CrudRepository<Vegetable, Long> {

    // 채소 이름으로 채소를 조회하는 메서드
    Optional<Vegetable> findByVegetableName(String vegetableName);

    // 채소 상태를 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Vegetable v SET v.status = :status WHERE v.uid = :uid")
    void updateStatusByUid(Long uid, String status);

    // 채소 이름으로 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM Vegetable WHERE vegetable_name = :vegetableName")
    void deleteByVegetableName(String vegetableName);

    // 채소 이름이 존재하는지 확인하는 메서드
    boolean existsByVegetableName(String vegetableName);
}
