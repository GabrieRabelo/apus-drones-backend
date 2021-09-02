package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
