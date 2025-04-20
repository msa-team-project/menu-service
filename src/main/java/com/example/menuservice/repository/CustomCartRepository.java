package com.example.menuservice.repository;

import com.example.menuservice.domain.CustomCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomCartRepository extends JpaRepository<CustomCart, Long> {

    // ✅ CustomCart ID로 조회
    Optional<CustomCart> findById(Long id);



    // ✅ CustomCart의 ID가 존재하는지 확인
    boolean existsById(Long id);
}
