package com.project.domain.payment.repository;

import com.project.domain.payment.PaymentStatus;
import com.project.domain.payment.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payments, Long> {
    List<Payments> findByStatus(PaymentStatus status);
}
