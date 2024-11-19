package com.project.domain.products.cursor.strategy;

import com.project.common.enums.Condition;
import com.project.domain.products.QProducts;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PriceHighCursorStrategy implements CursorStrategy {
    @Override
    public Condition getCondition() {
        return Condition.PRICE_HIGH;
    }
    @Override
    public StringTemplate generateCursor(QProducts products) {
        return Expressions.stringTemplate(
                "CONCAT(LPAD(CAST({0} AS STRING), 10, '0'), LPAD(CAST({1} AS STRING), 10, '0'))",
                products.price,
                products.id
        );
    }

    @Override
    public List<OrderSpecifier<?>> getSortOrders(QProducts products) {
        List<OrderSpecifier<?>> sortOrders = new ArrayList<>();
        sortOrders.add(products.price.desc());
        sortOrders.add(products.id.desc());
        return sortOrders;
    }
}