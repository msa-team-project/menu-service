package com.example.menuservice.repository;

import com.example.menuservice.domain.Cheese;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CheeseRepository extends CrudRepository<Cheese, Long> {

    // 치즈 이름으로 치즈를 조회하는 메서드
    Optional<Cheese> findByCheeseName(String cheeseName);

    // 치즈 상태를 업데이트하는 메서드
    @Modifying
    @Transactional
    @Query("UPDATE Cheese c SET c.status = :status WHERE c.uid = :uid")
    void updateStatusByUid(Long uid, String status);

    // 치즈 이름으로 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM Cheese WHERE cheese_name = :cheeseName")
    void deleteByCheeseName(String cheeseName);

    // 치즈 이름이 존재하는지 확인하는 메서드
    boolean existsByCheeseName(String cheeseName);
}
