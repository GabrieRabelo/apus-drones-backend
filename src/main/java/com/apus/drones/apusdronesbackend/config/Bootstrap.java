package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class Bootstrap {

    public final UserRepository userRepository;
    public final ProductRepository productRepository;

    public Bootstrap(UserRepository userRepository, ProductRepository productRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
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

        var products = ProductEntity.builder()
                .user(user)
                .weight(2D)
                .status(ProductStatus.ACTIVE)
                .name("Rabelo")
                .price(BigDecimal.ONE)
                .createDate(LocalDateTime.now())
                .build();

        productRepository.save(products);
    }
}
