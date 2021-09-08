package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.request.CreateProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductAPI {
    ResponseEntity<Void> create(CreateProductRequest request);

    //ResponseEntity<ProductEntity> get();
}
