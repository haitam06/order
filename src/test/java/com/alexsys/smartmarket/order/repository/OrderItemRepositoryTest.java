package com.alexsys.smartmarket.order.repository;

import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    private OrderDetails testOrder;

    @BeforeEach
    void setUp() {
        // Create a parent order
        testOrder = new OrderDetails();
        testOrder.setUserId(1);
        testOrder.setPaymentId(100);
        testOrder.setTotal(250.0);
        testOrder = orderDetailsRepository.save(testOrder);
    }

    @Test
    void save_shouldPersistOrderItem() {
        OrderItem item = new OrderItem();
        item.setOrderDetails(testOrder);
        item.setProductId(10);
        item.setProductsSkuId(101);
        item.setQuantity(2);

        OrderItem savedItem = orderItemRepository.save(item);

        assertNotNull(savedItem.getId());
        assertEquals(10, savedItem.getProductId());
        assertEquals(101, savedItem.getProductsSkuId());
        assertEquals(2, savedItem.getQuantity());
        assertEquals(testOrder.getId(), savedItem.getOrderDetails().getId());
    }

    @Test
    void findById_shouldReturnItem() {
        OrderItem item = new OrderItem();
        item.setOrderDetails(testOrder);
        item.setProductId(20);
        item.setQuantity(5);
        OrderItem savedItem = orderItemRepository.save(item);

        Optional<OrderItem> foundItem = orderItemRepository.findById(savedItem.getId());

        assertTrue(foundItem.isPresent());
        assertEquals(20, foundItem.get().getProductId());
        assertEquals(5, foundItem.get().getQuantity());
    }

    @Test
    void findAll_shouldReturnAllItems() {
        OrderItem item1 = new OrderItem();
        item1.setOrderDetails(testOrder);
        item1.setProductId(30);
        item1.setQuantity(3);

        OrderItem item2 = new OrderItem();
        item2.setOrderDetails(testOrder);
        item2.setProductId(40);
        item2.setQuantity(4);

        orderItemRepository.save(item1);
        orderItemRepository.save(item2);

        List<OrderItem> items = orderItemRepository.findAll();

        assertEquals(2, items.size());
    }

    @Test
    void delete_shouldRemoveItem() {
        OrderItem item = new OrderItem();
        item.setOrderDetails(testOrder);
        item.setProductId(50);
        item.setQuantity(1);
        OrderItem savedItem = orderItemRepository.save(item);

        orderItemRepository.deleteById(savedItem.getId());

        Optional<OrderItem> deletedItem = orderItemRepository.findById(savedItem.getId());
        assertFalse(deletedItem.isPresent());
    }

    @Test
    void update_shouldModifyItem() {
        OrderItem item = new OrderItem();
        item.setOrderDetails(testOrder);
        item.setProductId(60);
        item.setQuantity(7);
        OrderItem savedItem = orderItemRepository.save(item);

        savedItem.setQuantity(10);
        savedItem.setProductsSkuId(202);
        OrderItem updatedItem = orderItemRepository.save(savedItem);

        assertEquals(10, updatedItem.getQuantity());
        assertEquals(202, updatedItem.getProductsSkuId());
        assertEquals(savedItem.getId(), updatedItem.getId());
    }

    @Test
    void findByOrderDetailsId_shouldReturnItemsForOrder() {
        OrderItem item1 = new OrderItem();
        item1.setOrderDetails(testOrder);
        item1.setProductId(70);
        item1.setQuantity(2);

        OrderItem item2 = new OrderItem();
        item2.setOrderDetails(testOrder);
        item2.setProductId(80);
        item2.setQuantity(5);

        orderItemRepository.save(item1);
        orderItemRepository.save(item2);

        List<OrderItem> orderItems = orderItemRepository.findByOrderDetailsId(testOrder.getId());

        assertEquals(2, orderItems.size());
        assertTrue(orderItems.stream().anyMatch(i -> i.getProductId() == 70));
        assertTrue(orderItems.stream().anyMatch(i -> i.getProductId() == 80));
    }
}
