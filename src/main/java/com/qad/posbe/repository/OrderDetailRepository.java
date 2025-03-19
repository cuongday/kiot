package com.qad.posbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.qad.posbe.domain.OrderDetail;
import com.qad.posbe.domain.Order;
import com.qad.posbe.domain.Product;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {
    List<OrderDetail> findByOrder(Order order);
    List<OrderDetail> findByProduct(Product product);
} 