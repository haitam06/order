package com.alexsys.smartmarket.order.service;

import com.alexsys.smartmarket.order.mapper.PaymentDetailsMapper;
import com.alexsys.smartmarket.order.model.PaymentDetails;
import com.alexsys.smartmarket.order.repository.PaymentDetailsRepository;

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
class PaymentDetailsServiceTest {

    @Mock
    private PaymentDetailsRepository paymentDetailsRepository;

    @Mock
    private PaymentDetailsMapper paymentDetailsMapper;

    @InjectMocks
    private PaymentDetailsService paymentDetailsService;

    private PaymentDetails paymentDetails;

    @BeforeEach
    void setUp() {
        paymentDetails = new PaymentDetails();
        paymentDetails.setId(1);
        paymentDetails.setOrderId(10);
        paymentDetails.setOrderDetailsId(100);
        paymentDetails.setAmount(250.0);
        paymentDetails.setProvider("Stripe");
        paymentDetails.setStatus("PAID");
    }

    @Test
    void testGetAllPaymentsDetails() {
        when(paymentDetailsRepository.findAll()).thenReturn(Arrays.asList(paymentDetails));

        List<PaymentDetails> result = paymentDetailsService.getAllPaymentsDetails();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualTo(250.0);
        verify(paymentDetailsRepository, times(1)).findAll();
    }

    @Test
    void testGetPaymentDetailsById() {
        when(paymentDetailsRepository.findById(1)).thenReturn(Optional.of(paymentDetails));

        Optional<PaymentDetails> result = paymentDetailsService.getPaymentDetailsById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getProvider()).isEqualTo("Stripe");
        verify(paymentDetailsRepository, times(1)).findById(1);
    }

    @Test
    void testGetPaymentsByOrderId() {
        when(paymentDetailsRepository.findByOrderDetailsId(100)).thenReturn(Arrays.asList(paymentDetails));

        List<PaymentDetails> result = paymentDetailsService.getPaymentsByOrderId(100);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderDetailsId()).isEqualTo(100);
        verify(paymentDetailsRepository, times(1)).findByOrderDetailsId(100);
    }

    @Test
    void testCreatePayment() {
        when(paymentDetailsRepository.save(paymentDetails)).thenReturn(paymentDetails);

        PaymentDetails result = paymentDetailsService.createPayment(paymentDetails);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("PAID");
        verify(paymentDetailsRepository, times(1)).save(paymentDetails);
    }

    @Test
    void testUpdatePaymentDetails_Success() {
        PaymentDetails updatedDetails = new PaymentDetails();
        updatedDetails.setAmount(500.0);

        when(paymentDetailsRepository.findById(1)).thenReturn(Optional.of(paymentDetails));
        doAnswer(invocation -> {
            PaymentDetails existing = invocation.getArgument(0);
            PaymentDetails newDetails = invocation.getArgument(1);
            existing.setAmount(newDetails.getAmount());
            return null;
        }).when(paymentDetailsMapper).update(any(PaymentDetails.class), any(PaymentDetails.class));
        when(paymentDetailsRepository.save(any(PaymentDetails.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<PaymentDetails> result = paymentDetailsService.updatePaymentDetails(1, updatedDetails);

        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualTo(500.0);
        verify(paymentDetailsRepository, times(1)).findById(1);
        verify(paymentDetailsRepository, times(1)).save(paymentDetails);
    }

    @Test
    void testUpdatePaymentDetails_NotFound() {
        when(paymentDetailsRepository.findById(999)).thenReturn(Optional.empty());

        Optional<PaymentDetails> result = paymentDetailsService.updatePaymentDetails(999, paymentDetails);

        assertThat(result).isEmpty();
        verify(paymentDetailsRepository, never()).save(any(PaymentDetails.class));
    }

    @Test
    void testDeletePayment() {
        doNothing().when(paymentDetailsRepository).deleteById(1);

        paymentDetailsService.deletePayment(1);

        verify(paymentDetailsRepository, times(1)).deleteById(1);
    }
}
