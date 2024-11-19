package com.project.domain.products.cursor.strategy;

import com.project.common.enums.Condition;
import com.project.domain.products.QProducts;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewCursorStrategy implements CursorStrategy {
    @Override
    public Condition getCondition() {
        return Condition.NEW;
    }
    @Override
    public StringTemplate generateCursor(QProducts products) {
        return Expressions.stringTemplate(
                "CONCAT(LPAD(CAST({0} AS STRING), 10, '0'), LPAD(CAST({1} AS STRING), 10, '0'))",
                products.id,
                products.id
        );
    }

    @Override
    public List<OrderSpecifier<?>> getSortOrders(QProducts products) {
        return List.of(products.id.desc());
    }
}