package com.qad.posbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.User;
import com.qad.posbe.util.constant.PaymentMethod;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUser(User user);
    List<Order> findByPaymentMethod(PaymentMethod paymentMethod);

    
} 