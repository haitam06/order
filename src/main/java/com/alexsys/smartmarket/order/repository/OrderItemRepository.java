package com.alexsys.smartmarket.order.repository;

import com.alexsys.smartmarket.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderDetailsId(Integer orderId);
}
