package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import org.junit.jupiter.api.AfterEach;
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
    @AfterEach
    void cleanState() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindAllProductsByUserId() {

        var user = UserEntity.builder().name("rabelo").build();

        var anotherUser = UserEntity.builder().name("baza").build();

        final var savedUser = userRepository.save(user);
        userRepository.save(anotherUser);
        userRepository.flush();

        var product = ProductEntity.builder().user(user).name("Carregador de Hiphone").status(ProductStatus.ACTIVE)
            .productImages(List.of()).deleted(Boolean.FALSE).build();

        var anotherPartnerProduct = ProductEntity.builder().user(anotherUser).name("Carregador de Xiaomi")
            .status(ProductStatus.INACTIVE).deleted(Boolean.FALSE).build();

        var savedProduct = productRepository.save(product);
        productRepository.save(anotherPartnerProduct);
        productRepository.flush();

        var result = productRepository
            .findAllByUserIdAndStatusAndDeletedFalse(
                savedUser.getId(),
                ProductStatus.ACTIVE
            ).get(0);

        assertThat(result).usingRecursiveComparison().ignoringFields("user.productEntity").isEqualTo(savedProduct);
    }
}
