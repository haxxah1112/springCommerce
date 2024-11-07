package com.project.domain.products.repository;

import com.project.domain.products.StockLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockLogRepository extends JpaRepository<StockLogs, Long> {
}
