package com.alexsys.smartmarket.order.controller;

import com.alexsys.smartmarket.order.model.PaymentDetails;
import com.alexsys.smartmarket.order.service.PaymentDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/smartmarket/payments")
public class PaymentDetailsController {

    private final PaymentDetailsService paymentDetailsService;

    public PaymentDetailsController(PaymentDetailsService paymentDetailsService) {
        this.paymentDetailsService = paymentDetailsService;
    }

    @GetMapping
    public List<PaymentDetails> getAllPayments() {
        return paymentDetailsService.getAllPaymentsDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetails> getPaymentById(@PathVariable Integer id) {
        return paymentDetailsService.getPaymentDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentDetails> getPaymentsByOrderId(@PathVariable Integer orderId) {
        return paymentDetailsService.getPaymentsByOrderId(orderId);
    }

    @PostMapping
    public PaymentDetails createPayment(@RequestBody PaymentDetails paymentDetails) {
        return paymentDetailsService.createPayment(paymentDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDetails> updatePaymentDetails(@PathVariable("id") Integer id, @RequestBody PaymentDetails paymentDetailsDetails) {
        var updatedPaymentDetails = paymentDetailsService.updatePaymentDetails(id, paymentDetailsDetails);
        return updatedPaymentDetails.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentDetailsService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
