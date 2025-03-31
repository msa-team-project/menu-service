package com.example.menuservice.repository;

import com.example.menuservice.domain.Sauce;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SauceRepository extends CrudRepository<Sauce, Long> {

    // 소스 이름으로 소스를 조회하는 메서드
    Optional<Sauce> findBySauceName(String sauceName);

    // 소스 상태를 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Sauce s SET s.status = :status WHERE s.uid = :uid")
    void updateStatusByUid(Long uid, String status);

    // 소스 이름으로 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM Sauce WHERE sauce_name = :sauceName")
    void deleteBySauceName(String sauceName);

    // 소스 이름이 존재하는지 확인하는 메서드
    boolean existsBySauceName(String sauceName);
}
