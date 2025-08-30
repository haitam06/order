package com.alexsys.smartmarket.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "payment_details")
@Setter
@Getter
public class PaymentDetails {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id", nullable = false)
    private Integer orderId; 

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "provider", nullable = true)
    private String provider;

    @Column(name = "status", nullable = true)
    private String status;

    @Column(name = "order_details_id")
    private Integer orderDetailsId;
}
