package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;

public class ProductDtoMapper {

    public static ProductDTO fromProductEntity(ProductEntity productEntity) {

        ProductImage mainImg = productEntity.getProductImages()
                .stream()
                .filter(ProductImage::isMain)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Produto de id " + productEntity.getId() + " sem imagem principal cadastrada"));

        return ProductDTO.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .price(productEntity.getPrice())
                .status(productEntity.getStatus())
                .weight(productEntity.getWeight())
                .imageUrl(mainImg.getUrl())
                .build();
    }
}
