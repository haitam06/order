package com.alexsys.smartmarket.order.service;

import com.alexsys.smartmarket.order.mapper.OrderItemMapper;
import com.alexsys.smartmarket.order.model.OrderItem;
import com.alexsys.smartmarket.order.repository.OrderItemRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> getOrderItemById(Integer id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderItemRepository.findByOrderDetailsId(orderId);
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public Optional<OrderItem> updateOrderItem(Integer id, OrderItem orderItemDetails) {
        var existingOrderItemOptional = getOrderItemById(id);
        if (existingOrderItemOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingOrderItem = existingOrderItemOptional.get();
        orderItemMapper.update(existingOrderItem, orderItemDetails);
        return Optional.ofNullable(orderItemRepository.save(existingOrderItem));
    }

    public void deleteOrderItem(Integer id) {
        orderItemRepository.deleteById(id);
    }
}
