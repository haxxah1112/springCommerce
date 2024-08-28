package com.project.kafka;

import com.project.ApiApplication;
import com.project.domain.brands.BrandStatus;
import com.project.domain.brands.Brands;
import com.project.domain.brands.repository.BrandRepository;
import com.project.domain.order.OrderItems;
import com.project.domain.order.OrderStatus;
import com.project.domain.order.Orders;
import com.project.domain.order.repository.OrderItemRepository;
import com.project.domain.order.repository.OrderRepository;
import com.project.domain.products.Categories;
import com.project.domain.products.Products;
import com.project.domain.products.repository.ProductRepository;
import com.project.domain.users.UserRole;
import com.project.domain.users.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.fixture.BrandFixture;
import com.project.fixture.OrderFixture;
import com.project.fixture.ProductFixture;
import com.project.fixture.UserFixture;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = ApiApplication.class)
@DirtiesContext
@Transactional
public class OrderRollbackListenerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private UserRepository userRepository;

    Orders order;

    @BeforeEach
    public void setup() {
        Users user = UserFixture.createUser("userName", "email", UserRole.SELLER);
        userRepository.save(user);

        Brands brand = BrandFixture.createBrand("brandName", user);
        brandRepository.save(brand);

        Products product1 = ProductFixture.createProduct("top", brand, 200, Categories.TOP, 30);
        Products product2 = ProductFixture.createProduct("bag", brand, 1000, Categories.BAG, 30);
        productRepository.saveAll(List.of(product1, product2));

        order = OrderFixture.createOrder(user);
        orderRepository.save(order);

        List<OrderItems> orderItems = new ArrayList<>();
        orderItems.add(OrderFixture.createOrderItem(order, product1, 1));
        orderItems.add(OrderFixture.createOrderItem(order, product2, 2));
        orderItemRepository.saveAll(orderItems);
    }

    @Test
    public void orderRollbackTest() {
        kafkaTemplate.send("order-rollback", String.valueOf(order.getId()));

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    assertThat(orderRepository.findById(order.getId())).isNotPresent();
                    assertThat(orderItemRepository.findByOrderId(order.getId())).isEmpty();
                });
    }
}
