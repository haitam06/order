package com.alexsys.smartmarket.order.repository;

import com.alexsys.smartmarket.order.model.OrderDetails;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByUserId(Integer userId);
}