package com.project.domain.products;

import com.project.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "stock_log")
public class StockLogs extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long productId;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private StockLogStatus status;

    private String errorMessage;

    public void complete() {
        this.status = StockLogStatus.COMPLETED;
    }

    public void fail(String errorMessage) {
        this.status = StockLogStatus.FAIL;
        this.errorMessage = errorMessage;
    }

}
