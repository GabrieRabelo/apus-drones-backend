package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import org.apache.catalina.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper.fromUserEntity;

public class ProductDtoMapper {

    public static ProductDTO fromProductEntity(ProductEntity productEntity) {
        Optional<ProductImage> mainImg = productEntity.getProductImages().stream().filter(ProductImage::getIsMain).findFirst();
        String mainImgUrl = mainImg.map(ProductImage::getUrl).orElse(null);

        return ProductDTO.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .status(productEntity.getStatus())
                .weight(productEntity.getWeight())
                .createdAt(productEntity.getCreateDate())
                .imageUrl(mainImgUrl)
                .imagesUrls(productEntity.getProductImages().stream().map(ProductImage::getUrl).collect(Collectors.toList()))
                .partner(fromUserEntity(productEntity.getUser()))
                .quantity(productEntity.getQuantity()).build();
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
