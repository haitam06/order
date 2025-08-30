package com.alexsys.smartmarket.order.mapper;

import com.alexsys.smartmarket.order.model.PaymentDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentDetailsMapperTest {

    private PaymentDetailsMapper paymentDetailsMapper;

    private PaymentDetails originalPayment;
    private PaymentDetails updatedPayment;

    @BeforeEach
    void setUp() {
        paymentDetailsMapper = Mappers.getMapper(PaymentDetailsMapper.class);

        // Original PaymentDetails
        originalPayment = new PaymentDetails();
        originalPayment.setId(1);
        originalPayment.setOrderId(100);
        originalPayment.setAmount(250.0);
        originalPayment.setProvider("PayPal");
        originalPayment.setStatus("PENDING");
        originalPayment.setOrderDetailsId(10);

        // Updated PaymentDetails
        updatedPayment = new PaymentDetails();
        updatedPayment.setAmount(500.0);       // changed
        updatedPayment.setStatus("COMPLETED"); // changed
        updatedPayment.setProvider(null);      // should be ignored
        updatedPayment.setOrderId(null);       // should be ignored
        updatedPayment.setOrderDetailsId(null);// should be ignored
    }

    @Test
    void testUpdate_shouldUpdateNonNullFieldsOnly() {
        paymentDetailsMapper.update(originalPayment, updatedPayment);

        assertThat(originalPayment.getId()).isEqualTo(1); // unchanged
        assertThat(originalPayment.getAmount()).isEqualTo(500.0); // updated
        assertThat(originalPayment.getStatus()).isEqualTo("COMPLETED"); // updated
        assertThat(originalPayment.getProvider()).isEqualTo("PayPal"); // unchanged
        assertThat(originalPayment.getOrderId()).isEqualTo(100); // unchanged
        assertThat(originalPayment.getOrderDetailsId()).isEqualTo(10); // unchanged
    }

    @Test
    void testUpdate_withNullSource_doesNothing() {
        PaymentDetails beforeUpdate = new PaymentDetails();
        beforeUpdate.setId(2);
        beforeUpdate.setOrderId(200);
        beforeUpdate.setAmount(1000.0);
        beforeUpdate.setProvider("Stripe");
        beforeUpdate.setStatus("PENDING");
        beforeUpdate.setOrderDetailsId(20);

        paymentDetailsMapper.update(beforeUpdate, new PaymentDetails()); // all fields null

        assertThat(beforeUpdate.getId()).isEqualTo(2);
        assertThat(beforeUpdate.getOrderId()).isEqualTo(200);
        assertThat(beforeUpdate.getAmount()).isEqualTo(1000.0);
        assertThat(beforeUpdate.getProvider()).isEqualTo("Stripe");
        assertThat(beforeUpdate.getStatus()).isEqualTo("PENDING");
        assertThat(beforeUpdate.getOrderDetailsId()).isEqualTo(20);
    }
}
