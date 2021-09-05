package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testFindAllProductsByUserId() {

        var product = ProductEntity
                .builder()
                .id(1L)
                .userId(1L)
                .name("Carregador de Hiphone")
                .status(ProductStatus.ACTIVE)
                .build();

        var anotherPartnerProduct = ProductEntity.builder()
                .id(2324L)
                .userId(50L)
                .name("Carregador de Xiaomi")
                .status(ProductStatus.ACTIVE)
                .build();

        productRepository.save(product);
        productRepository.save(anotherPartnerProduct);
        productRepository.flush();

        var result = productRepository.findAllByUserIdAndStatus(1L, ProductStatus.ACTIVE);
        var expectedResult = List.of(product);

        assertThat(result).usingRecursiveFieldByFieldElementComparator().hasSameElementsAs(expectedResult);
    }
}
