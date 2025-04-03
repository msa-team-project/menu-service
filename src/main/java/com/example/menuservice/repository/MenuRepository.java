package com.example.menuservice.repository;

import com.example.menuservice.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    // ✅ 메뉴 이름으로 메뉴 조회
    Optional<Menu> findByMenuName(String menuName);



    // ✅ 메뉴 이름 존재 여부 확인
    boolean existsByMenuName(String menuName);
}
