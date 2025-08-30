package com.alexsys.smartmarket.order.mapper;

import com.alexsys.smartmarket.order.model.OrderDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDetailsMapperTest {

    private OrderDetailsMapper orderDetailsMapper;

    private OrderDetails originalOrder;
    private OrderDetails updatedOrder;

    @BeforeEach
    void setUp() {
        orderDetailsMapper = Mappers.getMapper(OrderDetailsMapper.class);

        // Original order
        originalOrder = new OrderDetails();
        originalOrder.setId(1);
        originalOrder.setUserId(100);
        originalOrder.setPaymentId(200);
        originalOrder.setTotal(500.0);
        originalOrder.setOrderItems(new ArrayList<>());

        // Updated order
        updatedOrder = new OrderDetails();
        updatedOrder.setUserId(101); // only userId changed
        updatedOrder.setTotal(600.0); // total changed
        // paymentId is null, should be ignored by the mapper
        updatedOrder.setPaymentId(null);
    }

    @Test
    void testUpdate_shouldUpdateNonNullFieldsOnly() {
        orderDetailsMapper.update(originalOrder, updatedOrder);

        assertThat(originalOrder.getId()).isEqualTo(1); // id unchanged
        assertThat(originalOrder.getUserId()).isEqualTo(101); // updated
        assertThat(originalOrder.getTotal()).isEqualTo(600.0); // updated
        assertThat(originalOrder.getPaymentId()).isEqualTo(200); // unchanged, because updatedOrder.paymentId is null
    }

    @Test
    void testUpdate_withNullSource_doesNothing() {
        OrderDetails beforeUpdate = new OrderDetails();
        beforeUpdate.setId(5);
        beforeUpdate.setUserId(10);
        beforeUpdate.setPaymentId(15);
        beforeUpdate.setTotal(100.0);

        orderDetailsMapper.update(beforeUpdate, new OrderDetails()); // all fields null

        assertThat(beforeUpdate.getId()).isEqualTo(5);
        assertThat(beforeUpdate.getUserId()).isEqualTo(10);
        assertThat(beforeUpdate.getPaymentId()).isEqualTo(15);
        assertThat(beforeUpdate.getTotal()).isEqualTo(100.0);
    }

    @Test
    void testUpdate_withEmptyOrderItems() {
        originalOrder.setOrderItems(null);
        updatedOrder.setOrderItems(new ArrayList<>());

        orderDetailsMapper.update(originalOrder, updatedOrder);

        assertThat(originalOrder.getOrderItems()).isEmpty();
    }
}
