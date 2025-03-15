package com.qad.posbe.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qad.posbe.domain.Supplier;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final CloudinaryService cloudinaryService;

    public Supplier handleCreateSupplier(Supplier supplier, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = this.cloudinaryService.uploadImage(imageFile);
            supplier.setImage(imageUrl);
        }
        return supplierRepository.save(supplier);
    }

    public void handleDeleteSupplier(Long id) {
        this.supplierRepository.deleteById(id);
    }

    public Supplier fetchSupplierById(Long id) {
        Optional<Supplier> supplierOptional = this.supplierRepository.findById(id);
        if (supplierOptional.isPresent()) {
            return supplierOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO handleGetSupplier(Specification<Supplier> supplierSpec, Pageable pageable) {
        Page<Supplier> pageSupplier = this.supplierRepository.findAll(supplierSpec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageSupplier.getTotalPages());
        meta.setTotal(pageSupplier.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageSupplier.getContent());
        return rs;
    }

    public Supplier handleUpdateSupplier(Long id, Supplier supplier, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = this.cloudinaryService.uploadImage(imageFile);
            supplier.setImage(imageUrl);
        }
        return this.supplierRepository.save(supplier);
    }

    public boolean existsByName(String name) {
        return this.supplierRepository.existsByName(name);
    }
}
