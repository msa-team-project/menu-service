package com.example.menuservice.repository;

import com.example.menuservice.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import feign.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // ✅ 메뉴 이름으로 메뉴 조회
    Optional<Menu> findByMenuName(String menuName);

    // ✅ 메뉴 상태로 조회 (status는 String)
    @Query("SELECT m FROM Menu m WHERE m.status = :status")
    List<Menu> findByStatus(@Param("status") String status);

    // ✅ 메뉴 이름 존재 여부 확인
    boolean existsByMenuName(String menuName);
}
