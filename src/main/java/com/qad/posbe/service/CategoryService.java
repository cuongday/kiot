package com.qad.posbe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.qad.posbe.domain.Category;
import com.qad.posbe.domain.response.ResultPaginationDTO;
import com.qad.posbe.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public Category handleCreateCategory(Category category, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = this.cloudinaryService.uploadImage(imageFile);
            category.setImage(imageUrl);
        }      
        return categoryRepository.save(category);
    }

    @Transactional
    public void handleDeleteCategory(Long id) {
        this.categoryRepository.deleteById(id);
    }

    public Category fetchCategoryById(Long id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        if(categoryOptional.isPresent()) {
            return categoryOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO handleGetCategory(Specification<Category> categorySpec, Pageable pageable) {
        Page<Category> pageCategory = this.categoryRepository.findAll(categorySpec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageCategory.getTotalPages());
        meta.setTotal(pageCategory.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(pageCategory.getContent());
        return rs;
    }

    @Transactional
    public Category handleUpdateCategory(Long id, Category category, MultipartFile imageFile) {
        Category existingCategory = this.fetchCategoryById(id);
        if (existingCategory != null) {
            existingCategory.setName(category.getName());
            existingCategory.setDescription(category.getDescription());
            
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = this.cloudinaryService.uploadImage(imageFile);
                existingCategory.setImage(imageUrl);
            }
            
            return this.categoryRepository.save(existingCategory);
        }
        return null;
    }

    public boolean existsByName(String name) {
        return this.categoryRepository.existsByName(name);
    }
} 