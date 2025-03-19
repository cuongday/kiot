package com.qad.posbe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.qad.posbe.domain.Product;
import com.qad.posbe.domain.Category;
import com.qad.posbe.domain.Supplier;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategory(Category category);
    List<Product> findBySupplier(Supplier supplier);
    Product findByName(String name);
    boolean existsByName(String name);
    Page<Product> findAll(Pageable pageable);
} 