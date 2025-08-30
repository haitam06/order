package com.alexsys.smartmarket.order.service;

import com.alexsys.smartmarket.order.mapper.OrderItemMapper;
import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.model.OrderItem;
import com.alexsys.smartmarket.order.repository.OrderItemRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderItemService orderItemService;

    private OrderItem orderItem;
    private OrderDetails orderDetails;

    @BeforeEach
    void setUp() {
        orderDetails = new OrderDetails();
        orderDetails.setId(1);
        orderDetails.setUserId(100);

        orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setOrderDetails(orderDetails);
        orderItem.setProductId(200);
        orderItem.setProductsSkuId(300);
        orderItem.setQuantity(2);
    }

    @Test
    void testGetAllOrderItems() {
        when(orderItemRepository.findAll()).thenReturn(Arrays.asList(orderItem));

        List<OrderItem> result = orderItemService.getAllOrderItems();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo(200);
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderItemById() {
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        Optional<OrderItem> result = orderItemService.getOrderItemById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getQuantity()).isEqualTo(2);
        verify(orderItemRepository, times(1)).findById(1);
    }

    @Test
    void testGetOrderItemsByOrderId() {
        when(orderItemRepository.findByOrderDetailsId(1)).thenReturn(Arrays.asList(orderItem));

        List<OrderItem> result = orderItemService.getOrderItemsByOrderId(1);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderDetails().getId()).isEqualTo(1);
        verify(orderItemRepository, times(1)).findByOrderDetailsId(1);
    }

    @Test
    void testCreateOrderItem() {
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);

        OrderItem result = orderItemService.createOrderItem(orderItem);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(200);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void testUpdateOrderItem_Success() {
        OrderItem updatedDetails = new OrderItem();
        updatedDetails.setQuantity(5);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
        doAnswer(invocation -> {
            OrderItem existing = invocation.getArgument(0);
            OrderItem newDetails = invocation.getArgument(1);
            existing.setQuantity(newDetails.getQuantity());
            return null;
        }).when(orderItemMapper).update(any(OrderItem.class), any(OrderItem.class));
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<OrderItem> result = orderItemService.updateOrderItem(1, updatedDetails);

        assertThat(result).isPresent();
        assertThat(result.get().getQuantity()).isEqualTo(5);
        verify(orderItemRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    void testUpdateOrderItem_NotFound() {
        when(orderItemRepository.findById(999)).thenReturn(Optional.empty());

        Optional<OrderItem> result = orderItemService.updateOrderItem(999, orderItem);

        assertThat(result).isEmpty();
        verify(orderItemRepository, never()).save(any(OrderItem.class));
    }

    @Test
    void testDeleteOrderItem() {
        doNothing().when(orderItemRepository).deleteById(1);

        orderItemService.deleteOrderItem(1);

        verify(orderItemRepository, times(1)).deleteById(1);
    }
}
