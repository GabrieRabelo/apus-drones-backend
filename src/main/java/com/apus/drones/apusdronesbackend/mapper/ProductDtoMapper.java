package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper.fromUserEntity;

public class ProductDtoMapper {

    public static List<ProductDTO> fromProductEntityList(List<ProductEntity> entity) {
        var response = new ArrayList<ProductDTO>();

        for (ProductEntity product : entity) {

            var dto = fromProductEntity(product);

            response.add(dto);
        }
        return response;
    }

    public static ProductDTO fromProductEntity(ProductEntity productEntity) {
        Optional<ProductImage> mainImg =
            productEntity.getProductImages().stream().filter(ProductImage::getIsMain).findFirst();
        String mainImgUrl = mainImg.map(ProductImage::getUrl).orElse(null);

        List<String> imagesUrls = new ArrayList<>();
        if (mainImgUrl != null) {
            imagesUrls.add(mainImgUrl);
        }
        imagesUrls.addAll(productEntity.getProductImages().stream()
            .filter(p -> p.getId() != null && !p.getId().equals(
                mainImg.map(ProductImage::getId).orElse(0L)))
            .map(ProductImage::getUrl)
            .collect(Collectors.toList())
        );

        return ProductDTO.builder()
            .id(productEntity.getId())
            .name(productEntity.getName())
            .description(productEntity.getDescription())
            .price(productEntity.getPrice())
            .status(productEntity.getStatus())
            .weight(productEntity.getWeight())
            .createdAt(productEntity.getCreateDate())
            .deleted(productEntity.getDeleted())
            .imageUrl(mainImgUrl)
            .imagesUrls(imagesUrls)
            .partner(fromUserEntity(productEntity.getUser()))
            .quantity(productEntity.getQuantity()).build();
    }
}
