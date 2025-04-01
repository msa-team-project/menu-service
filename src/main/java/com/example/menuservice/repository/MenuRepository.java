package com.example.menuservice.repository;

import com.example.menuservice.domain.Menu;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

    // 메뉴 이름으로 메뉴를 조회하는 메서드
    Optional<Menu> findByMenuName(String menuName);

    // 메뉴 상태를 업데이트하는 메서드 (예시: 상태 변경)
    @Modifying
    @Transactional
    @Query("UPDATE Menu m SET m.status = :status WHERE m.uid = :uid")
    void updateStatusByUid(Long uid, String status);

    // 메뉴 이름으로 삭제하는 메서드
    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.menu_name = :menuName")
    void deleteByMenuName(String menuName);

    // 메뉴 이름이 존재하는지 확인하는 메서드
    boolean existsByMenuName(String menuName);
}
