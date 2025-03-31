package com.example.menuservice.repository;

import com.example.menuservice.domain.Bread;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BreadRepository extends CrudRepository<Bread, Long> {

    // 빵 이름으로 빵을 조회하는 메서드
    Optional<Bread> findByBreadName(String breadName);

    // 빵 상태를 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Bread b SET b.status = :status WHERE b.uid = :uid")
    void updateStatusByUid(Long uid, String status);


    // 빵 이름으로 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM Bread WHERE bread_name = :breadName")
    void deleteByBreadName(String breadName);

    // 빵 이름이 존재하는지 확인하는 메서드
    boolean existsByBreadName(String breadName);
}
