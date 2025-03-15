package com.qad.posbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.qad.posbe.domain.SupplierCategory;
import com.qad.posbe.domain.Supplier;
import com.qad.posbe.domain.Category;

import java.util.List;

public interface SupplierCategoryRepository extends JpaRepository<SupplierCategory, Long>, JpaSpecificationExecutor<SupplierCategory> {
    List<SupplierCategory> findBySupplier(Supplier supplier);
    List<SupplierCategory> findByCategory(Category category);
    SupplierCategory findBySupplierAndCategory(Supplier supplier, Category category);
} 