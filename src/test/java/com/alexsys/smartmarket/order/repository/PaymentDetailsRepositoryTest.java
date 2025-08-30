package com.alexsys.smartmarket.order.repository;

import com.alexsys.smartmarket.order.model.PaymentDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PaymentDetailsRepositoryTest {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    private PaymentDetails testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new PaymentDetails();
        testPayment.setOrderId(1);
        testPayment.setAmount(150.0);
        testPayment.setProvider("VISA");
        testPayment.setStatus("PENDING");
        testPayment.setOrderDetailsId(10);
    }

    @Test
    void save_shouldPersistPayment() {
        PaymentDetails savedPayment = paymentDetailsRepository.save(testPayment);

        assertNotNull(savedPayment.getId());
        assertEquals(1, savedPayment.getOrderId());
        assertEquals(150.0, savedPayment.getAmount());
        assertEquals("VISA", savedPayment.getProvider());
        assertEquals("PENDING", savedPayment.getStatus());
        assertEquals(10, savedPayment.getOrderDetailsId());
    }

    @Test
    void findById_shouldReturnPayment() {
        PaymentDetails savedPayment = paymentDetailsRepository.save(testPayment);

        Optional<PaymentDetails> foundPayment = paymentDetailsRepository.findById(savedPayment.getId());

        assertTrue(foundPayment.isPresent());
        assertEquals(savedPayment.getId(), foundPayment.get().getId());
        assertEquals("VISA", foundPayment.get().getProvider());
    }

    @Test
    void findAll_shouldReturnAllPayments() {
        paymentDetailsRepository.save(testPayment);

        PaymentDetails anotherPayment = new PaymentDetails();
        anotherPayment.setOrderId(2);
        anotherPayment.setAmount(300.0);
        anotherPayment.setProvider("MASTERCARD");
        anotherPayment.setStatus("SUCCESS");
        anotherPayment.setOrderDetailsId(20);
        paymentDetailsRepository.save(anotherPayment);

        List<PaymentDetails> payments = paymentDetailsRepository.findAll();

        assertEquals(2, payments.size());
    }

    @Test
    void delete_shouldRemovePayment() {
        PaymentDetails savedPayment = paymentDetailsRepository.save(testPayment);

        paymentDetailsRepository.deleteById(savedPayment.getId());
        Optional<PaymentDetails> deletedPayment = paymentDetailsRepository.findById(savedPayment.getId());

        assertFalse(deletedPayment.isPresent());
    }

    @Test
    void update_shouldModifyPayment() {
        PaymentDetails savedPayment = paymentDetailsRepository.save(testPayment);

        savedPayment.setStatus("SUCCESS");
        savedPayment.setAmount(500.0);
        PaymentDetails updatedPayment = paymentDetailsRepository.save(savedPayment);

        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals(500.0, updatedPayment.getAmount());
        assertEquals(savedPayment.getId(), updatedPayment.getId());
    }

    @Test
    void findByOrderDetailsId_shouldReturnPaymentsForOrderDetails() {
        paymentDetailsRepository.save(testPayment);

        PaymentDetails anotherPayment = new PaymentDetails();
        anotherPayment.setOrderId(3);
        anotherPayment.setAmount(400.0);
        anotherPayment.setProvider("PAYPAL");
        anotherPayment.setStatus("FAILED");
        anotherPayment.setOrderDetailsId(10); // same orderDetailsId
        paymentDetailsRepository.save(anotherPayment);

        List<PaymentDetails> foundPayments = paymentDetailsRepository.findByOrderDetailsId(10);

        assertEquals(2, foundPayments.size());
        assertTrue(foundPayments.stream().allMatch(p -> p.getOrderDetailsId() == 10));
    }
}
