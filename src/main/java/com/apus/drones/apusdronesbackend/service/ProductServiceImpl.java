package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.request.CreateProductRequest;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<ProductDTO> findAllProductsByUserId(Long userId) {
        var resultFromDB = productRepository.findAllByUserIdAndStatus(userId, ProductStatus.ACTIVE);
        var responseList = new ArrayList<ProductDTO>();

        for (ProductEntity product: resultFromDB) {


            var dto = ProductDTO.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .build();
            responseList.add(dto);
        }
        return responseList;
    }
}
