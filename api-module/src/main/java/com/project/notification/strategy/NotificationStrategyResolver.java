package com.project.notification.strategy;

import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationStrategyResolver {
    private final Map<String, NotificationStrategy> strategyMap = new HashMap<>();

    public NotificationStrategyResolver(List<NotificationStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getKey(), strategy));
    }

    public NotificationStrategy resolveStrategy(String key) {
        NotificationStrategy strategy = strategyMap.get(key);
        if (strategy == null) {
            throw new NotFoundException(CustomError.NOT_FOUND, "No strategy found for key: " + key);
        }
        return strategy;
    }
}
