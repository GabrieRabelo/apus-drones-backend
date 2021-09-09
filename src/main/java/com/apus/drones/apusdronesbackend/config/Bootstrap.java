package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class Bootstrap {

    public final UserRepository userRepository;
    public final ProductRepository productRepository;
    public final ProductImageRepository productImageRepository;

    public Bootstrap(UserRepository userRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Bean
    public void initDatabase() {
        initUsers();
        initProducts();
    }

    private void initUsers() {
        var user = UserEntity.builder()
                .name("Rabelo")
                .role(Role.CUSTOMER)
                .avatarUrl("none")
                .cpfCnpj("12312312312")
                .password("blublu")
                .email("rabelo@rab.elo")
                .build();

        userRepository.save(user);
    }

    private void initProducts() {
        var user = userRepository.findById(1L).orElse(null);
        var productImage = ProductImage.builder().isMain(true).url("www.image.com.br").build();

        var product = ProductEntity.builder()
                .user(user)
                .weight(2D)
                .status(ProductStatus.ACTIVE)
                .name("Rabelo")
                .price(BigDecimal.ONE)
                .createDate(LocalDateTime.now())
                .productImages(List.of(productImage))
                .build();

        productImage.setProduct(product);
        productRepository.save(product);


        var productImage1 = ProductImage.builder().isMain(true).url("www.image2.com.br").build();

        var product1 = ProductEntity.builder()
                .user(user)
                .weight(3D)
                .status(ProductStatus.ACTIVE)
                .name("Teste123")
                .price(BigDecimal.TEN)
                .createDate(LocalDateTime.now())
                .productImages(List.of(productImage1))
                .build();

        productImage1.setProduct(product1);
        productRepository.save(product1);
    }
}
