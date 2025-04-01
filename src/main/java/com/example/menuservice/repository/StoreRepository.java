package com.example.menuservice.repository;

import com.example.menuservice.domain.Store;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StoreRepository extends CrudRepository<Store, Integer> {
    //지점 uid로 지점을 조회하는 메서드
    Optional<Store> findByUid(int uid);

    // 지점 상태를 업데이트하는 메서드 (예시: 상태 변경)
    @Modifying
    @Transactional
    @Query("UPDATE store s SET s.status = :status WHERE s.uid= :uid")
    void updateStatusByUid(@Param("uid") int uid, @Param("status") String status);

    // 지점 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM store s WHERE s.uid = :uid")
    void deleteByUid(@Param("uid") int uid);

    // 지점 이름이 존재하는지 확인하는 메서드
    boolean existsByStoreName(String storeName);

}
