package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.request.CreateProductRequest;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Void> create(CreateProductRequest request);

    List<ProductDTO> findAllActiveProductsByUserId(Long userId);
}
