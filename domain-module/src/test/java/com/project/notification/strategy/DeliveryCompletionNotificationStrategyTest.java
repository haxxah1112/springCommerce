package com.project.notification.strategy;

import com.project.BrandFixture;
import com.project.OrderFixture;
import com.project.ProductFixture;
import com.project.UserFixture;
import com.project.common.message.DeliveryCompletionNotificationMessage;
import com.project.domain.brands.Brands;
import com.project.domain.order.OrderItems;
import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.users.Users;
import com.project.notification.sender.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryCompletionNotificationStrategyTest {
    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private NotificationSender notificationSender;

    @Mock
    private NotificationStrategyResolver notificationStrategyResolver;

    @BeforeEach
    void setUp() {
        NotificationStrategy deliveryCompletionNotificationStrategy = new DeliveryCompletionNotificationStrategy(orderItemRepository, notificationSender);
        NotificationStrategy eventNotificationStrategy = new EventNotificationStrategy(notificationSender);

        notificationStrategyResolver = new NotificationStrategyResolver(
                List.of(deliveryCompletionNotificationStrategy, eventNotificationStrategy)
        );
    }

    @Test
    void deliveryCompletionNotificationStrategy_Handle_Success() {
        //Given
        Users user = UserFixture.createDefaultUser();
        Long orderItemId = 1L;
        DeliveryCompletionNotificationMessage message = new DeliveryCompletionNotificationMessage(user, orderItemId);

        Orders order = OrderFixture.createOrder(user, OrderStatus.COMPLETED);
        Brands brand = BrandFixture.createBrand("brandTest", user);
        Products product = ProductFixture.createProduct("itemName", brand, 2000, Categories.TOP, 100);
        OrderItems orderItem = OrderFixture.createOrderItem(product, 1);

        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItem));
        NotificationStrategy strategy = notificationStrategyResolver.resolveStrategy("DELIVERY_COMPLETION_TEMPLATE");

        //When
        strategy.handle(message);

        //Then
        verify(orderItemRepository, times(1)).findById(1L);
        verify(notificationSender, times(1)).send(
                eq(message.getUser().getPhone()),
                argThat(params -> params.containsKey("itemName") && params.get("itemName").equals(orderItem.getProduct().getName()))
        );
    }
}