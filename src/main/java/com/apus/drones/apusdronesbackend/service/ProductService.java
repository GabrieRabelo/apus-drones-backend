package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    ResponseEntity<Void> create(CreateProductDTO productDTO);

    ProductDTO get(Long id);

    ResponseEntity<Void> update(Long id, ProductDTO productDTO);

    ResponseEntity<Void> delete(Long id);

    List<ProductDTO> findAllActiveProductsByUserId(Long userId);
}
