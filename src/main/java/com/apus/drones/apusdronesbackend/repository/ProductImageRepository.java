package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    ProductImage findProductImageByProductAndUrl(ProductEntity productEntity, String imageUrl);

    List<ProductImage> findProductImageByProduct(ProductEntity productEntity);

    void deleteAllByProduct(ProductEntity productEntity);
}
