package com.example.menuservice.repository;

import com.example.menuservice.domain.Side;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SideRepository extends CrudRepository<Side, Long> {

    // 사이드 이름으로 사이드를 조회하는 메서드
    Optional<Side> findBySideName(String sideName);

    // 사이드 상태를 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Side s SET s.status = :status WHERE s.uid = :uid")
    void updateStatusByUid(Long uid, String status);

    // 사이드 이름으로 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM Side WHERE side_name = :sideName")
    void deleteBySideName(String sideName);

    // 사이드 이름이 존재하는지 확인하는 메서드
    boolean existsBySideName(String sideName);
}
