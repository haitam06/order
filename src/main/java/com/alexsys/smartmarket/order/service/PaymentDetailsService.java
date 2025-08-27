package com.alexsys.smartmarket.order.service;

import com.alexsys.smartmarket.order.mapper.PaymentDetailsMapper;
import com.alexsys.smartmarket.order.model.PaymentDetails;
import com.alexsys.smartmarket.order.repository.PaymentDetailsRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentDetailsService {

    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PaymentDetailsMapper paymentDetailsMapper;

    public PaymentDetailsService(PaymentDetailsRepository paymentDetailsRepository,
                                 PaymentDetailsMapper paymentDetailsMapper) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.paymentDetailsMapper = paymentDetailsMapper;
    }

    public List<PaymentDetails> getAllPaymentsDetails() {
        return paymentDetailsRepository.findAll();
    }
    public Optional<PaymentDetails> getPaymentDetailsById(Integer id) {
        return paymentDetailsRepository.findById(id);
    }

    
    public List<PaymentDetails> getPaymentsByOrderId(Integer orderId) {
        return paymentDetailsRepository.findByOrderDetailsId(orderId);
    }

    public PaymentDetails createPayment(PaymentDetails paymentDetails) {
        return paymentDetailsRepository.save(paymentDetails);
    }

    public Optional<PaymentDetails> updatePaymentDetails(Integer id, PaymentDetails paymentDetailsDetails) {
        var existingPaymentDetailsOptional = getPaymentDetailsById(id);
        if (existingPaymentDetailsOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingPaymentDetails = existingPaymentDetailsOptional.get();
        paymentDetailsMapper.update(existingPaymentDetails, paymentDetailsDetails);
        return Optional.ofNullable(paymentDetailsRepository.save(existingPaymentDetails));
    }
    public void deletePayment(Integer id) {
        paymentDetailsRepository.deleteById(id);
    }
}
