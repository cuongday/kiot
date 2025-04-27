package com.qad.posbe.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.qad.posbe.domain.Product;
import com.qad.posbe.domain.request.CreateProductDTO;
import com.qad.posbe.domain.request.UpdateProductDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    // Ánh xạ từ CreateProductDTO sang Product
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "buyPrice", target = "buyPrice")
    @Mapping(source = "sellPrice", target = "sellPrice")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "importDetails", ignore = true)
    Product toEntity(CreateProductDTO dto);
    
    // Cập nhật Product từ UpdateProductDTO
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "buyPrice", target = "buyPrice")
    @Mapping(source = "sellPrice", target = "sellPrice")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "importDetails", ignore = true)
    void updateEntityFromDto(UpdateProductDTO dto, @MappingTarget Product product);
} 