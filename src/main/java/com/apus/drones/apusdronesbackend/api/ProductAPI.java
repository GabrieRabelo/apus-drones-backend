package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.request.product.CreateProductRequest;
import com.apus.drones.apusdronesbackend.model.request.product.UpdateProductRequest;
import com.apus.drones.apusdronesbackend.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductAPI {
    private final ProductService productService;

    public ProductAPI(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated CreateProductRequest request) {
        log.info("Received a new create product request for product name [{}]", request.getName());

        return productService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> get(@PathVariable Long id) {
        log.info("Received a new get product request for product id [{}]", id);

        return productService.get(id);
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody @Validated UpdateProductRequest request) {
        log.info("Received a new update product request for product id [{}]", request.getProductId());

        return productService.update(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received a new delete product request for product id [{}]", id);

        return productService.delete(id);
    }


}
