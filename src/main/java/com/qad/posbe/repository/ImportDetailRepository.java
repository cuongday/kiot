package com.qad.posbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.qad.posbe.domain.ImportDetail;
import com.qad.posbe.domain.ImportHistory;
import com.qad.posbe.domain.Product;

import java.util.List;

@Repository
public interface ImportDetailRepository extends JpaRepository<ImportDetail, Long>, JpaSpecificationExecutor<ImportDetail> {
    List<ImportDetail> findByImportHistory(ImportHistory importHistory);
    List<ImportDetail> findByProduct(Product product);
} 