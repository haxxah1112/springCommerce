package com.project.domain.products.cursor.strategy;

import com.project.common.enums.Condition;
import com.project.domain.products.QProducts;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.StringTemplate;

import java.util.List;

public interface CursorStrategy {
    Condition getCondition();
    StringTemplate generateCursor(QProducts products);
    List<OrderSpecifier<?>> getSortOrders(QProducts products);
}
