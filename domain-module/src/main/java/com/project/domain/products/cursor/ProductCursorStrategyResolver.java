package com.project.domain.products.cursor;

import com.project.common.enums.Condition;
import com.project.domain.products.cursor.strategy.*;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductCursorStrategyResolver {
    private final Map<Condition, CursorStrategy> strategyMap = new HashMap<>();

    public ProductCursorStrategyResolver(List<CursorStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getCondition(), strategy));
    }

    public CursorStrategy resolveStrategy(Condition key) {
        CursorStrategy strategy = strategyMap.get(key);
        if (strategy == null) {
            throw new NotFoundException(CustomError.NOT_FOUND, "No strategy found for key: " + key);
        }
        return strategy;
    }

}
