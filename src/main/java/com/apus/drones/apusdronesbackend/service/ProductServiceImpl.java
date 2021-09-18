package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.request.product.CreateProductRequest;
import com.apus.drones.apusdronesbackend.model.request.product.UpdateProductRequest;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper.fromProductEntityList;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<Void> create(CreateProductRequest request) {
        ProductEntity entity = ProductEntity.builder()
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .weight(request.getWeight())
                .build();

        Long generatedId = productRepository.save(entity).getId();

        log.info("Saved new product entity with id [{}]", generatedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductEntity> get(Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível encontrar o produto com ID " + id));
    }

    @Override
    public List<ProductDTO> findAllActiveProductsByUserId(Long userId) {
        var resultFromDB = productRepository.findAllByUserIdAndStatus(userId, ProductStatus.ACTIVE);
        return fromProductEntityList(resultFromDB);
    }

    @Override
    public ResponseEntity<Void> update(UpdateProductRequest request) {
        ProductEntity entity =
                productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível encontrar o produto com ID " + request.getProductId()));

        updateProduct(request, entity);

        productRepository.save(entity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        ProductEntity entity =
                productRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível encontrar o produto com ID " + id));

        productRepository.delete(entity);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void updateProduct(UpdateProductRequest request, ProductEntity entity) {
        if (request.getName() != null)
            entity.setName(request.getName());

        if (request.getPrice() != null)
            entity.setPrice(request.getPrice());

        if (request.getStatus() != null)
            entity.setStatus(request.getStatus());

        if (request.getWeight() != null)
            entity.setWeight(request.getWeight());
    }
}
