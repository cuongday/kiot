package com.qad.posbe.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.qad.posbe.domain.Supplier;
import com.qad.posbe.domain.mapper.SupplierMapper;
import com.qad.posbe.domain.request.CreateSupplierDTO;
import com.qad.posbe.domain.request.UpdateSupplierDTO;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.service.SupplierService;
import com.qad.posbe.util.annotation.ApiMessage;
import com.qad.posbe.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;

    @GetMapping("/suppliers")
    @ApiMessage("Get all suppliers")
    public ResponseEntity<ResultPaginationDTO> getSuppliers(
        @Filter Specification<Supplier> supplierSpec,
        Pageable pageable
    ) {
        ResultPaginationDTO rs = this.supplierService.handleGetSupplier(supplierSpec, pageable);
        return ResponseEntity.ok(rs);
    }

    @PostMapping(value = "/suppliers", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiMessage("Create new supplier")
    public ResponseEntity<Supplier> createSupplier(
        @Valid @ModelAttribute CreateSupplierDTO supplierDTO,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IdInvalidException {
        boolean isNameExist = this.supplierService.existsByName(supplierDTO.getName());
        if(isNameExist) {
            throw new IdInvalidException("Tên nhà cung cấp đã tồn tại");
        }
        
        Supplier supplier = this.supplierMapper.toEntity(supplierDTO);
        
        Supplier newSupplier = this.supplierService.handleCreateSupplier(supplier, imageFile, supplierDTO.getCategoryIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(newSupplier);
    }

    @PutMapping(value = "/suppliers/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiMessage("Update supplier by id")
    public ResponseEntity<Supplier> updateSupplier(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute UpdateSupplierDTO supplierDTO,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IdInvalidException {
        Supplier supplier = this.supplierService.fetchSupplierById(id);
        if(supplier == null) {
            throw new IdInvalidException("Nhà cung cấp với id = " + id + " không tồn tại");
        }   
        this.supplierMapper.updateEntityFromDto(supplierDTO, supplier);
        Supplier updatedSupplier = this.supplierService.handleUpdateSupplier(id, supplier, imageFile, supplierDTO.getCategoryIds());
        return ResponseEntity.ok(updatedSupplier);
    }

    @DeleteMapping("/suppliers/{id}")
    @ApiMessage("Delete supplier by id")
    public ResponseEntity<Void> deleteSupplier(@PathVariable("id") Long id) throws IdInvalidException {
        Supplier currentSupplier = this.supplierService.fetchSupplierById(id);
        if(currentSupplier == null) {
            throw new IdInvalidException("Nhà cung cấp với id = " + id + " không tồn tại");
        }
        this.supplierService.handleDeleteSupplier(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/suppliers/{id}")
    @ApiMessage("Get supplier by id")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable("id") Long id) throws IdInvalidException {
        Supplier supplier = this.supplierService.fetchSupplierById(id);
        if(supplier == null) {
            throw new IdInvalidException("Nhà cung cấp với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(supplier);
    }
    
    @GetMapping("/suppliers/{id}/categories")
    @ApiMessage("Get categories of supplier")
    public ResponseEntity<List<Long>> getSupplierCategories(@PathVariable("id") Long id) throws IdInvalidException {
        Supplier supplier = this.supplierService.fetchSupplierById(id);
        if(supplier == null) {
            throw new IdInvalidException("Nhà cung cấp với id = " + id + " không tồn tại");
        }
        List<Long> categoryIds = this.supplierService.getCategoryIdsBySupplier(id);
        return ResponseEntity.ok(categoryIds);
    }
}
