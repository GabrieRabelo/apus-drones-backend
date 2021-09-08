package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
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

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testFindAllProductsByUserId() {

        var user = UserEntity.builder().name("rabelo").build();

        var anotherUser = UserEntity.builder().name("baza").build();

        userRepository.save(user);
        userRepository.save(anotherUser);
        userRepository.flush();

        var product = ProductEntity
                .builder()
                .user(user)
                .name("Carregador de Hiphone")
                .status(ProductStatus.ACTIVE)
                .productImages(List.of())
                .build();

        var anotherPartnerProduct = ProductEntity.builder()
                .user(anotherUser)
                .name("Carregador de Xiaomi")
                .status(ProductStatus.INACTIVE)
                .build();

        productRepository.save(product);
        productRepository.save(anotherPartnerProduct);
        productRepository.flush();

        var result = productRepository.findAllByUserIdAndStatus(2L, ProductStatus.ACTIVE);
        var expectedResult = List.of(product);

        assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("id")
                .hasSameElementsAs(expectedResult);
    }
}
