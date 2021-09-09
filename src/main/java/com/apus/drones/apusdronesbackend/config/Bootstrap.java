package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
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
    public final OrderRepository orderRepository;

    public Bootstrap(UserRepository userRepository, ProductRepository productRepository, OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Bean
    public void initDatabase() {
        initUsers();
        initProducts();
        initOrders();
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

        var user2 = UserEntity.builder()
                .name("Gabriel")
                .role(Role.PARTNER)
                .avatarUrl("none")
                .cpfCnpj("12312312312")
                .password("blublu")
                .email("rabelo@rab.elo")
                .build();

        userRepository.save(user2);
    }

    private void initProducts() {
        var products = ProductEntity.builder()
                .weight(2D)
                .status(ProductStatus.ACTIVE)
                .name("Rabelo")
                .price(BigDecimal.ONE)
                .createDate(LocalDateTime.now())
                .build();

        productRepository.save(products);
    }

    private void initOrders() {
        var order = OrderEntity.builder()
                .customer(userRepository.findAllByRole(Role.CUSTOMER).get(0))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(0)) //TODO
                .status(OrderStatus.ACCEPTED)
                .createdAt(LocalDateTime.now())
                .deliveryPrice(new BigDecimal("50"))
                .orderPrice(new BigDecimal("50"))
                .build();

        orderRepository.save(order);
    }
}
