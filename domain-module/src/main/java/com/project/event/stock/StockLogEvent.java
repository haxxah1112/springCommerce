package com.project.event.stock;

import com.project.common.dto.StockDecrementResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class StockLogEvent {
    private final List<StockDecrementResult> stockDecrementResults;
    private final Long orderId;
}
