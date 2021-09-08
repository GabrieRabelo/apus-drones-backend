package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.request.CreateProductRequest;
import com.apus.drones.apusdronesbackend.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductAPI {
    private final ProductService productService;

    public ProductAPI(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    public ResponseEntity<Void> create(@RequestBody @Validated CreateProductRequest request) {
        log.info("Received a new create product request for product name [{}]", request.getName());

        return productService.create(request);
    }

    @GetMapping("partners/{partnerId}/products")
    public ResponseEntity<List<ProductEntity>> findAllProductsByPartnerId(@PathVariable Long partnerId) {
        var response =  productService.findAllActiveProductsByUserId(partnerId);

        return ResponseEntity.ok(response);
    }
}
