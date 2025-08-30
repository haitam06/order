package com.alexsys.smartmarket.order.mapper;

import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemMapperTest {

    private OrderItemMapper orderItemMapper;

    private OrderItem originalItem;
    private OrderItem updatedItem;

    @BeforeEach
    void setUp() {
        orderItemMapper = Mappers.getMapper(OrderItemMapper.class);

        // Original OrderItem
        originalItem = new OrderItem();
        originalItem.setId(1);
        originalItem.setProductId(10);
        originalItem.setProductsSkuId(100);
        originalItem.setQuantity(5);
        originalItem.setOrderDetails(new OrderDetails());

        // Updated OrderItem
        updatedItem = new OrderItem();
        updatedItem.setProductId(20);  // changed
        updatedItem.setQuantity(10);   // changed
        updatedItem.setProductsSkuId(null); // should be ignored
        updatedItem.setOrderDetails(null);  // should be ignored
    }

    @Test
    void testUpdate_shouldUpdateNonNullFieldsOnly() {
        orderItemMapper.update(originalItem, updatedItem);

        assertThat(originalItem.getId()).isEqualTo(1); // id unchanged
        assertThat(originalItem.getProductId()).isEqualTo(20); // updated
        assertThat(originalItem.getQuantity()).isEqualTo(10); // updated
        assertThat(originalItem.getProductsSkuId()).isEqualTo(100); // unchanged
        assertThat(originalItem.getOrderDetails()).isNotNull(); // unchanged
    }

    @Test
    void testUpdate_withNullSource_doesNothing() {
        OrderItem beforeUpdate = new OrderItem();
        beforeUpdate.setId(2);
        beforeUpdate.setProductId(5);
        beforeUpdate.setProductsSkuId(50);
        beforeUpdate.setQuantity(3);
        beforeUpdate.setOrderDetails(new OrderDetails());

        orderItemMapper.update(beforeUpdate, new OrderItem()); // all fields null

        assertThat(beforeUpdate.getId()).isEqualTo(2);
        assertThat(beforeUpdate.getProductId()).isEqualTo(5);
        assertThat(beforeUpdate.getProductsSkuId()).isEqualTo(50);
        assertThat(beforeUpdate.getQuantity()).isEqualTo(3);
        assertThat(beforeUpdate.getOrderDetails()).isNotNull();
    }
}
