package com.alexsys.smartmarket.order.controller;

import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.model.OrderItem;
import com.alexsys.smartmarket.order.service.OrderItemService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemControllerTest {

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderItemController orderItemController;

    private OrderItem orderItem;

    private OrderDetails orderDetails;

    @BeforeEach
    void setUp() {
        orderDetails = new OrderDetails();
        orderDetails.setId(1);
        orderDetails.setUserId(1);
        orderDetails.setPaymentId(100);
        orderDetails.setTotal(250.0);

        orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setOrderDetails(orderDetails);
        orderItem.setProductId(10);
        orderItem.setQuantity(2);
        orderItem.setProductsSkuId(101);
    }

    @Test
    void testGetAllOrderItems() {
        when(orderItemService.getAllOrderItems()).thenReturn(Arrays.asList(orderItem));

        List<OrderItem> items = orderItemController.getAllOrderItems();

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getId()).isEqualTo(1);
        verify(orderItemService, times(1)).getAllOrderItems();
    }

    @Test
    void testGetOrderItemById_Found() {
        when(orderItemService.getOrderItemById(1)).thenReturn(Optional.of(orderItem));

        ResponseEntity<OrderItem> response = orderItemController.getOrderItemById(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(orderItem);
        verify(orderItemService, times(1)).getOrderItemById(1);
    }

    @Test
    void testGetOrderItemById_NotFound() {
        when(orderItemService.getOrderItemById(999)).thenReturn(Optional.empty());

        ResponseEntity<OrderItem> response = orderItemController.getOrderItemById(999);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
        verify(orderItemService, times(1)).getOrderItemById(999);
    }

    @Test
    void testGetOrderItemsByOrderId() {
        when(orderItemService.getOrderItemsByOrderId(1)).thenReturn(Arrays.asList(orderItem));

        List<OrderItem> items = orderItemController.getOrderItemsByOrderId(1);

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getOrderDetails().getId()).isEqualTo(1);
        verify(orderItemService, times(1)).getOrderItemsByOrderId(1);
    }

    @Test
    void testCreateOrderItem() {
        when(orderItemService.createOrderItem(orderItem)).thenReturn(orderItem);

        OrderItem createdItem = orderItemController.createOrderItem(orderItem);

        assertThat(createdItem).isNotNull();
        assertThat(createdItem.getId()).isEqualTo(1);
        verify(orderItemService, times(1)).createOrderItem(orderItem);
    }

    @Test
    void testUpdateOrderItem_Found() {
        when(orderItemService.updateOrderItem(1, orderItem)).thenReturn(Optional.of(orderItem));

        ResponseEntity<OrderItem> response = orderItemController.updateOrderItem(1, orderItem);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(orderItem);
        verify(orderItemService, times(1)).updateOrderItem(1, orderItem);
    }

    @Test
    void testUpdateOrderItem_NotFound() {
        when(orderItemService.updateOrderItem(999, orderItem)).thenReturn(Optional.empty());

        ResponseEntity<OrderItem> response = orderItemController.updateOrderItem(999, orderItem);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
        verify(orderItemService, times(1)).updateOrderItem(999, orderItem);
    }

    @Test
    void testDeleteOrderItem() {
        doNothing().when(orderItemService).deleteOrderItem(1);

        ResponseEntity<Void> response = orderItemController.deleteOrderItem(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(orderItemService, times(1)).deleteOrderItem(1);
    }
}
