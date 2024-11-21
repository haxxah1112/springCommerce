package com.project.domain.products.repository;

import com.project.domain.products.StockLogStatus;
import com.project.domain.products.StockLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockLogRepository extends JpaRepository<StockLogs, Long> {


    List<StockLogs> findByProductIdAndStatus(Long productId, StockLogStatus status);

    List<StockLogs> findByStatus(StockLogStatus status);

    List<StockLogs> findByProductId(Long productId);
}
