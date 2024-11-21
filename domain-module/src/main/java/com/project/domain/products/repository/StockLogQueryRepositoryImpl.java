package com.project.domain.products.repository;

import com.project.domain.products.QStockLogs;
import com.project.domain.products.StockLogStatus;
import com.project.domain.products.StockLogs;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockLogQueryRepositoryImpl implements StockLogQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QStockLogs stockLog = QStockLogs.stockLogs;

    public List<StockLogs> findFailedAndPendingLogs() {
        return queryFactory.selectFrom(stockLog)
                .where(stockLog.status.in(StockLogStatus.FAIL, StockLogStatus.PENDING))
                .groupBy(stockLog.productId)
                .fetch();
    }
}
