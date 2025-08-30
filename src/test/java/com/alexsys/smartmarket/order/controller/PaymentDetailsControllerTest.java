package com.alexsys.smartmarket.order.controller;

import com.alexsys.smartmarket.order.model.PaymentDetails;
import com.alexsys.smartmarket.order.service.PaymentDetailsService;
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
class PaymentDetailsControllerTest {

    @Mock
    private PaymentDetailsService paymentDetailsService;

    @InjectMocks
    private PaymentDetailsController paymentDetailsController;

    private PaymentDetails paymentDetails;

    @BeforeEach
    void setUp() {
        paymentDetails = new PaymentDetails();
        paymentDetails.setId(1);
        paymentDetails.setOrderId(100);
        paymentDetails.setAmount(250.0);
        paymentDetails.setProvider("PayPal");
        paymentDetails.setStatus("PAID");
        paymentDetails.setOrderDetailsId(1);
    }

    @Test
    void testGetAllPayments() {
        when(paymentDetailsService.getAllPaymentsDetails()).thenReturn(Arrays.asList(paymentDetails));

        List<PaymentDetails> payments = paymentDetailsController.getAllPayments();

        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getId()).isEqualTo(1);
        verify(paymentDetailsService, times(1)).getAllPaymentsDetails();
    }

    @Test
    void testGetPaymentById_Found() {
        when(paymentDetailsService.getPaymentDetailsById(1)).thenReturn(Optional.of(paymentDetails));

        ResponseEntity<PaymentDetails> response = paymentDetailsController.getPaymentById(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(paymentDetails);
        verify(paymentDetailsService, times(1)).getPaymentDetailsById(1);
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentDetailsService.getPaymentDetailsById(999)).thenReturn(Optional.empty());

        ResponseEntity<PaymentDetails> response = paymentDetailsController.getPaymentById(999);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
        verify(paymentDetailsService, times(1)).getPaymentDetailsById(999);
    }

    @Test
    void testGetPaymentsByOrderId() {
        when(paymentDetailsService.getPaymentsByOrderId(100)).thenReturn(Arrays.asList(paymentDetails));

        List<PaymentDetails> payments = paymentDetailsController.getPaymentsByOrderId(100);

        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getOrderId()).isEqualTo(100);
        verify(paymentDetailsService, times(1)).getPaymentsByOrderId(100);
    }

    @Test
    void testCreatePayment() {
        when(paymentDetailsService.createPayment(paymentDetails)).thenReturn(paymentDetails);

        PaymentDetails createdPayment = paymentDetailsController.createPayment(paymentDetails);

        assertThat(createdPayment).isNotNull();
        assertThat(createdPayment.getId()).isEqualTo(1);
        verify(paymentDetailsService, times(1)).createPayment(paymentDetails);
    }

    @Test
    void testUpdatePaymentDetails_Found() {
        when(paymentDetailsService.updatePaymentDetails(1, paymentDetails)).thenReturn(Optional.of(paymentDetails));

        ResponseEntity<PaymentDetails> response = paymentDetailsController.updatePaymentDetails(1, paymentDetails);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(paymentDetails);
        verify(paymentDetailsService, times(1)).updatePaymentDetails(1, paymentDetails);
    }

    @Test
    void testUpdatePaymentDetails_NotFound() {
        when(paymentDetailsService.updatePaymentDetails(999, paymentDetails)).thenReturn(Optional.empty());

        ResponseEntity<PaymentDetails> response = paymentDetailsController.updatePaymentDetails(999, paymentDetails);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
        verify(paymentDetailsService, times(1)).updatePaymentDetails(999, paymentDetails);
    }

    @Test
    void testDeletePayment() {
        doNothing().when(paymentDetailsService).deletePayment(1);

        ResponseEntity<Void> response = paymentDetailsController.deletePayment(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(paymentDetailsService, times(1)).deletePayment(1);
    }
}
