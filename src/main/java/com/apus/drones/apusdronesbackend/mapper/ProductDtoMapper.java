package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.service.dto.ProductDto;

public class ProductDtoMapper {

    public static ProductDto fromProductEntity(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .createDate(productEntity.getCreateDate())
                .price(productEntity.getPrice())
                .status(productEntity.getStatus())
                .weight(productEntity.getWeight())
                .build();
    }
}
