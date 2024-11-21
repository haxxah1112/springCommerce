package com.project.domain.products.repository;

import com.project.domain.products.StockLogs;

import java.util.List;

public interface StockLogQueryRepository {
    List<StockLogs> findFailedAndPendingLogs();
}
