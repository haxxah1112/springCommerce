package com.project.notification.strategy;

import com.project.domain.order.OrderItems;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.enums.NotificationTemplate;
import com.project.exception.NotFoundException;
import com.project.exception.error.CustomError;
import com.project.common.message.DeliveryCompletionNotificationMessage;
import com.project.common.message.NotificationMessage;
import com.project.notification.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeliveryCompletionNotificationStrategy implements NotificationStrategy {
    private final OrderItemRepository orderItemRepository;
    private final NotificationSender notificationSender;

    @Override
    public NotificationTemplate getTemplate() {
        return NotificationTemplate.DELIVERY_COMPLETION_TEMPLATE;
    }

    @Override
    public void handle(NotificationMessage notificationMessage) {
        if (!(notificationMessage instanceof DeliveryCompletionNotificationMessage message)) {
            throw new IllegalArgumentException("Invalid request type for DeliveryCompletionNotificationStrategy");
        }

        OrderItems orderItem = orderItemRepository.findById(message.getOrderItemId())
                .orElseThrow(() -> new NotFoundException(CustomError.NOT_FOUND));

        Map<String, Object> params = message.toParams();
        params.put("itemName", orderItem.getProduct().getName());
        notificationSender.send(message.getUser().getPhone(), params);
    }
}
