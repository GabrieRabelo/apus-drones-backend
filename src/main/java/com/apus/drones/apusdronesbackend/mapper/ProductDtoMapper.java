package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;

import static com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper.fromUserEntity;

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
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .status(productEntity.getStatus())
                .weight(productEntity.getWeight())
                .createdAt(productEntity.getCreateDate())
                .imageUrl(mainImg.getUrl())
                .partner(fromUserEntity(productEntity.getUser()))
                .quantity(productEntity.getQuantity())
                .build();
    }

    public static List<ProductDTO> fromProductEntityList(List<ProductEntity> entity) {
        var response = new ArrayList<ProductDTO>();

        for (ProductEntity product : entity) {

            var dto = fromProductEntity(product);

            response.add(dto);
        }
        return response;
    }
}
