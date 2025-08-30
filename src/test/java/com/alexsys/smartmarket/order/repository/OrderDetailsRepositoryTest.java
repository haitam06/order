package com.alexsys.smartmarket.order.repository;

import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OrderDetailsRepositoryTest {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    private OrderDetails testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new OrderDetails();
        testOrder.setUserId(1);
        testOrder.setPaymentId(100);
        testOrder.setTotal(250.0);
    
        // optional: adding items if you want to test cascade
        List<OrderItem> items = new ArrayList<>();
        OrderItem item1 = new OrderItem();
        item1.setProductId(10);
        item1.setQuantity(2);
        // Removed setPrice since it doesn’t exist
        item1.setOrderDetails(testOrder); // maintain relationship
    
        items.add(item1);
        testOrder.setOrderItems(items);
    }
    
    @Test
    void save_shouldPersistOrder() {
        OrderDetails savedOrder = orderDetailsRepository.save(testOrder);

        assertNotNull(savedOrder.getId());
        assertEquals(1, savedOrder.getUserId());
        assertEquals(100, savedOrder.getPaymentId());
        assertEquals(250.0, savedOrder.getTotal());
        assertNotNull(savedOrder.getOrderItems());
        assertEquals(1, savedOrder.getOrderItems().size());
    }

    @Test
    void findById_shouldReturnOrder() {
        OrderDetails savedOrder = orderDetailsRepository.save(testOrder);

        Optional<OrderDetails> foundOrder = orderDetailsRepository.findById(savedOrder.getId());

        assertTrue(foundOrder.isPresent());
        assertEquals(savedOrder.getId(), foundOrder.get().getId());
        assertEquals(1, foundOrder.get().getUserId());
    }

    @Test
    void findAll_shouldReturnAllOrders() {
        orderDetailsRepository.save(testOrder);

        OrderDetails anotherOrder = new OrderDetails();
        anotherOrder.setUserId(2);
        anotherOrder.setPaymentId(200);
        anotherOrder.setTotal(500.0);
        orderDetailsRepository.save(anotherOrder);

        List<OrderDetails> orders = orderDetailsRepository.findAll();

        assertEquals(2, orders.size());
    }

    @Test
    void delete_shouldRemoveOrder() {
        OrderDetails savedOrder = orderDetailsRepository.save(testOrder);

        orderDetailsRepository.deleteById(savedOrder.getId());
        Optional<OrderDetails> deletedOrder = orderDetailsRepository.findById(savedOrder.getId());

        assertFalse(deletedOrder.isPresent());
    }

    @Test
    void update_shouldModifyOrder() {
        OrderDetails savedOrder = orderDetailsRepository.save(testOrder);

        savedOrder.setTotal(999.99);
        savedOrder.setPaymentId(300);
        OrderDetails updatedOrder = orderDetailsRepository.save(savedOrder);

        assertEquals(999.99, updatedOrder.getTotal());
        assertEquals(300, updatedOrder.getPaymentId());
        assertEquals(savedOrder.getId(), updatedOrder.getId());
    }

    @Test
    void findByUserId_shouldReturnOrdersForUser() {
        orderDetailsRepository.save(testOrder);

        OrderDetails otherUserOrder = new OrderDetails();
        otherUserOrder.setUserId(2);
        otherUserOrder.setPaymentId(500);
        otherUserOrder.setTotal(1000.0);
        orderDetailsRepository.save(otherUserOrder);

        List<OrderDetails> userOrders = orderDetailsRepository.findByUserId(1);

        assertEquals(1, userOrders.size());
        assertEquals(1, userOrders.get(0).getUserId());
    }
}
