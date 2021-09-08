package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.request.product.CreateProductRequest;
import com.apus.drones.apusdronesbackend.model.request.product.UpdateProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<Void> create(CreateProductRequest request);

    ResponseEntity<ProductEntity> get(Long id);

    ResponseEntity<Void> update(UpdateProductRequest request);

    ResponseEntity<Void> delete(Long id);
}
