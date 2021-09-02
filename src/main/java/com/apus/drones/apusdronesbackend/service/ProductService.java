package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.request.CreateProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<Void> create(CreateProductRequest request);
}
