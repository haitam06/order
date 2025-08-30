package com.alexsys.smartmarket.order.service;

import com.alexsys.smartmarket.order.mapper.OrderDetailsMapper;
import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.repository.OrderDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailsServiceTest {

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private OrderDetailsMapper orderDetailsMapper;

    @InjectMocks
    private OrderDetailsService orderDetailsService;

    private OrderDetails order1;
    private OrderDetails order2;

    @BeforeEach
    void setUp() {
        order1 = new OrderDetails();
        order1.setId(1);
        order1.setUserId(100);
        order1.setPaymentId(500);
        order1.setTotal(200.0);

        order2 = new OrderDetails();
        order2.setId(2);
        order2.setUserId(200);
        order2.setPaymentId(600);
        order2.setTotal(400.0);
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {
        when(orderDetailsRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<OrderDetails> result = orderDetailsService.getAllOrders();

        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getUserId());
        verify(orderDetailsRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_shouldReturnOrder_whenExists() {
        when(orderDetailsRepository.findById(1)).thenReturn(Optional.of(order1));

        Optional<OrderDetails> result = orderDetailsService.getOrderById(1);

        assertTrue(result.isPresent());
        assertEquals(100, result.get().getUserId());
        verify(orderDetailsRepository, times(1)).findById(1);
    }

    @Test
    void getOrderById_shouldReturnEmpty_whenNotExists() {
        when(orderDetailsRepository.findById(99)).thenReturn(Optional.empty());

        Optional<OrderDetails> result = orderDetailsService.getOrderById(99);

        assertFalse(result.isPresent());
        verify(orderDetailsRepository, times(1)).findById(99);
    }

    @Test
    void saveOrder_shouldSaveAndReturnOrder() {
        when(orderDetailsRepository.save(order1)).thenReturn(order1);

        OrderDetails saved = orderDetailsService.saveOrder(order1);

        assertNotNull(saved);
        assertEquals(100, saved.getUserId());
        verify(orderDetailsRepository, times(1)).save(order1);
    }

    @Test
    void getOrdersByUserId_shouldReturnOrdersForUser() {
        when(orderDetailsRepository.findByUserId(100)).thenReturn(List.of(order1));

        List<OrderDetails> result = orderDetailsService.getOrdersByUserId(100);

        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getUserId());
        verify(orderDetailsRepository, times(1)).findByUserId(100);
    }

    @Test
    void updateOrderDetails_shouldUpdateAndReturnUpdatedOrder() {
        OrderDetails newDetails = new OrderDetails();
        newDetails.setUserId(111);
        newDetails.setPaymentId(777);
        newDetails.setTotal(999.99);

        when(orderDetailsRepository.findById(1)).thenReturn(Optional.of(order1));
        when(orderDetailsRepository.save(order1)).thenReturn(order1);

        Optional<OrderDetails> updated = orderDetailsService.updateOrderDetails(1, newDetails);

        assertTrue(updated.isPresent());
        verify(orderDetailsMapper, times(1)).update(order1, newDetails);
        verify(orderDetailsRepository, times(1)).save(order1);
    }

    @Test
    void updateOrderDetails_shouldReturnEmpty_whenOrderNotExists() {
        when(orderDetailsRepository.findById(99)).thenReturn(Optional.empty());

        Optional<OrderDetails> updated = orderDetailsService.updateOrderDetails(99, order1);

        assertFalse(updated.isPresent());
        verify(orderDetailsMapper, never()).update(any(), any());
        verify(orderDetailsRepository, never()).save(any());
    }

    @Test
    void deleteOrder_shouldCallRepositoryDelete() {
        doNothing().when(orderDetailsRepository).deleteById(1);

        orderDetailsService.deleteOrder(1);

        verify(orderDetailsRepository, times(1)).deleteById(1);
    }
}
