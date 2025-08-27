package com.alexsys.smartmarket.order.service;

import com.alexsys.smartmarket.order.mapper.OrderDetailsMapper;
import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.repository.OrderDetailsRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailsMapper orderDetailsMapper;

    public OrderDetailsService(OrderDetailsRepository orderDetailsRepository,
                               OrderDetailsMapper orderDetailsMapper) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderDetailsMapper = orderDetailsMapper;
    }

    public List<OrderDetails> getAllOrders() {
        return orderDetailsRepository.findAll();
    }

    public Optional<OrderDetails> getOrderById(Integer id) {
        return orderDetailsRepository.findById(id);
    }

    public OrderDetails saveOrder(OrderDetails orderDetails) {
        return orderDetailsRepository.save(orderDetails);
    }

    
    public List<OrderDetails> getOrdersByUserId(Integer userId) {
        return orderDetailsRepository.findByUserId(userId);
    }

    public Optional<OrderDetails> updateOrderDetails(Integer id, OrderDetails orderDetailsDetails) {
        var existingOrderOptional = getOrderById(id);
        if (existingOrderOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingOrder = existingOrderOptional.get();
        orderDetailsMapper.update(existingOrder, orderDetailsDetails);
        return Optional.ofNullable(orderDetailsRepository.save(existingOrder));
    }

    public void deleteOrder(Integer id) {
        orderDetailsRepository.deleteById(id);
    }
}
