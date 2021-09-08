package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.request.CreateProductRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Void> create(CreateProductRequest request);

    List<ProductEntity> findAllActiveProductsByUserId(Long userId);
}
