package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByUserIdAndStatusAndDeletedFalse(Long userId, ProductStatus productStatus);

    List<ProductEntity> findAllByNameContainingIgnoreCaseAndStatus(String name, ProductStatus productStatus);

    Optional<ProductEntity> findByIdAndDeletedFalse(Long id);
}
