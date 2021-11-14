package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.ProductService;
import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.UpdateProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductAPI {
    private final ProductService productService;

    public ProductAPI(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> find(@RequestParam Long partner) {
        log.info("Received a new find product request");
        return ResponseEntity.ok(productService.findAllActiveProductsByUserId(partner));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Validated CreateProductDTO createProductDTO) {
        log.info("Received a new create product for product name [{}]", createProductDTO.getName());

        return productService.create(createProductDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        log.info("Received a new get product request for product id [{}]", id);

        return ResponseEntity.ok(productService.get(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id,
                                             @RequestBody @Validated UpdateProductDTO updateProductDTO) {
        log.info("Received a new update product productDTO for product id [{}]", id);

        return ResponseEntity.ok(productService.update(id, updateProductDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Received a new delete product request for product id [{}]", id);

        return productService.delete(id);
    }


}
