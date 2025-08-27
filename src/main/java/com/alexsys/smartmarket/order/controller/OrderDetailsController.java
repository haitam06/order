package com.alexsys.smartmarket.order.controller;

import com.alexsys.smartmarket.order.model.OrderDetails;
import com.alexsys.smartmarket.order.service.OrderDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/smartmarket/orders")
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;

    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    @GetMapping
    public List<OrderDetails> getAllOrders() {
        return orderDetailsService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetails> getOrderById(@PathVariable Integer id) {
        return orderDetailsService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<OrderDetails> getOrdersByUserId(@PathVariable Integer userId) {
        return orderDetailsService.getOrdersByUserId(userId);
    }

    @PostMapping
    public OrderDetails createOrder(@RequestBody OrderDetails order) {
        return orderDetailsService.saveOrder(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetails> updateOrderDetails(@PathVariable("id") Integer id, @RequestBody OrderDetails orderDetailsDetails) {
        var updatedOrderDetails = orderDetailsService.updateOrderDetails(id, orderDetailsDetails);
        return updatedOrderDetails.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderDetailsService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
