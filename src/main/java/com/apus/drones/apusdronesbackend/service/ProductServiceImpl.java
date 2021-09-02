package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.request.CreateProductRequest;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<Void> create(CreateProductRequest request) {
        ProductEntity entity = new ProductEntity(request.getName(), request.getPrice(), request.getStatus(), request.getWeight());

        Long generatedId =
                productRepository.save(entity).getId();

        log.info("Saved new product entity with id [{}]", generatedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
