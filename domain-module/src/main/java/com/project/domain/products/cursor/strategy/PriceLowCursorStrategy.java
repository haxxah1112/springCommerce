package com.project.domain.products.cursor.strategy;

import com.project.common.enums.Condition;
import com.project.domain.products.QProducts;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PriceLowCursorStrategy implements CursorStrategy {
    private static final long MAX_PRICE = 999999999L;

    @Override
    public Condition getCondition() {
        return Condition.PRICE_LOW;
    }

    @Override
    public StringTemplate generateCursor(QProducts products) {
        return Expressions.stringTemplate(
                "CONCAT(LPAD(CAST({0} AS STRING), 10, '0'), LPAD(CAST({1} AS STRING), 10, '0'))",
                Expressions.operation(Long.class, Ops.SUB, Expressions.constant(MAX_PRICE), products.price),
                products.id
        );
    }

    @Override
    public List<OrderSpecifier<?>> getSortOrders(QProducts products) {
        List<OrderSpecifier<?>> sortOrders = new ArrayList<>();
        sortOrders.add(products.price.asc());
        sortOrders.add(products.id.desc());
        return sortOrders;
    }
}
