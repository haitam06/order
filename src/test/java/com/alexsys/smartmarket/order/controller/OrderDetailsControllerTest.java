package com.alexsys.smartmarket.order.controller;

import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.service.OrderDetailsService;

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
class OrderDetailsControllerTest {

    @Mock
    private OrderDetailsService orderDetailsService;

    @InjectMocks
    private OrderDetailsController orderDetailsController;

    private OrderDetails orderDetails;

    @BeforeEach
    void setUp() {
        orderDetails = new OrderDetails();
        orderDetails.setId(1);
        orderDetails.setUserId(1);
        orderDetails.setPaymentId(100);
        orderDetails.setTotal(250.0);
    }

    @Test
    void testGetAllOrders() {
        when(orderDetailsService.getAllOrders()).thenReturn(Arrays.asList(orderDetails));

        List<OrderDetails> orders = orderDetailsController.getAllOrders();

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getId()).isEqualTo(1);
        verify(orderDetailsService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById_Found() {
        when(orderDetailsService.getOrderById(1)).thenReturn(Optional.of(orderDetails));

        ResponseEntity<OrderDetails> response = orderDetailsController.getOrderById(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(orderDetails);
        verify(orderDetailsService, times(1)).getOrderById(1);
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderDetailsService.getOrderById(999)).thenReturn(Optional.empty());

        ResponseEntity<OrderDetails> response = orderDetailsController.getOrderById(999);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
        verify(orderDetailsService, times(1)).getOrderById(999);
    }

    @Test
    void testGetOrdersByUserId() {
        when(orderDetailsService.getOrdersByUserId(1)).thenReturn(Arrays.asList(orderDetails));

        List<OrderDetails> orders = orderDetailsController.getOrdersByUserId(1);

        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getUserId()).isEqualTo(1);
        verify(orderDetailsService, times(1)).getOrdersByUserId(1);
    }

    @Test
    void testCreateOrder() {
        when(orderDetailsService.saveOrder(orderDetails)).thenReturn(orderDetails);

        OrderDetails createdOrder = orderDetailsController.createOrder(orderDetails);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isEqualTo(1);
        verify(orderDetailsService, times(1)).saveOrder(orderDetails);
    }

    @Test
    void testUpdateOrderDetails_Found() {
        when(orderDetailsService.updateOrderDetails(1, orderDetails)).thenReturn(Optional.of(orderDetails));

        ResponseEntity<OrderDetails> response = orderDetailsController.updateOrderDetails(1, orderDetails);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(orderDetails);
        verify(orderDetailsService, times(1)).updateOrderDetails(1, orderDetails);
    }

    @Test
    void testUpdateOrderDetails_NotFound() {
        when(orderDetailsService.updateOrderDetails(999, orderDetails)).thenReturn(Optional.empty());

        ResponseEntity<OrderDetails> response = orderDetailsController.updateOrderDetails(999, orderDetails);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
        verify(orderDetailsService, times(1)).updateOrderDetails(999, orderDetails);
    }

    @Test
    void testDeleteOrder() {
        doNothing().when(orderDetailsService).deleteOrder(1);

        ResponseEntity<Void> response = orderDetailsController.deleteOrder(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(orderDetailsService, times(1)).deleteOrder(1);
    }
}
