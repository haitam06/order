package com.alexsys.smartmarket.order.repository;

import com.alexsys.smartmarket.order.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Integer> {
    List<PaymentDetails> findByOrderDetailsId(Integer orderDetailsId);
}
