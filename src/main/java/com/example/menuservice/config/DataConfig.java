package com.example.menuservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.menuservice.repository") // JPA Repository 활성화
@EnableTransactionManagement // 트랜잭션 관리 활성화
public class DataConfig {
}
