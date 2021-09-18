package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.OrderItemEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.OrderItemRepository;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
@Profile({"dev","local","prd", "default"})
public class Bootstrap {

    private static int contEntities;
    public final UserRepository userRepository;
    public final ProductRepository productRepository;
    public final ProductImageRepository productImageRepository;
    public final OrderRepository orderRepository;
    public final OrderItemRepository orderItemRepository;

    public Bootstrap(UserRepository userRepository, ProductRepository productRepository, ProductImageRepository productImageRepository, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Bean
    public void initDatabase() {
        initUsers();
        initOrders();
    }

    private void initUsers() {
        populatePartners();

        var userCustomer = UserEntity.builder()
                .name("Rabelo")
                .role(Role.CUSTOMER)
                .avatarUrl("none")
                .cpfCnpj("12312312312")
                .password("blublu")
                .email("rabelo@rab.elo")
                .build();

        userRepository.save(userCustomer);
    }

    private void populatePartners() {
        for (int i = 0; i < 10; i++) {
            var user2 = UserEntity.builder()
                    .name("Parceiro " + contEntities)
                    .role(Role.PARTNER)
                    .avatarUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + contEntities + ".png")
                    .cpfCnpj("12312312312")
                    .password("blublu")
                    .email("rabelo@rab.elo")
                    .build();
            userRepository.save(user2);
            contEntities++;
            populateProducts(user2.getId());
        }
    }

    private void populateProducts(long id) {
        for (int i = 0; i < 5; i++) {
            var user = userRepository.findById(id).orElse(null);
            var productImage = ProductImage.builder().isMain(true).url("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + contEntities + ".png").build();

            var product = ProductEntity.builder()
                    .user(user)
                    .weight(2D)
                    .status(ProductStatus.ACTIVE)
                    .name("Produto " + contEntities)
                    .description("Lorem ipsum")
                    .price(BigDecimal.valueOf(new Random().nextInt(1000)))
                    .createDate(LocalDateTime.now())
                    .productImages(List.of(productImage))
                    .quantity(25)
                    .build();

            productImage.setProduct(product);
            productRepository.save(product);
            contEntities++;

        }
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

        var order2 = OrderEntity.builder()
                .customer(userRepository.findAllByRole(Role.CUSTOMER).get(0))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(0)) //TODO
                .status(OrderStatus.IN_CART)
                .createdAt(LocalDateTime.now())
                .deliveryPrice(new BigDecimal("50"))
                .orderPrice(new BigDecimal("50"))
                .build();

        var orderItems = OrderItemEntity.builder()
                .quantity(2)
                .price(new BigDecimal(50))
                .order(order)
                .product(productRepository.findAll().get(0))
                .build();

        orderRepository.save(order);
        orderRepository.save(order2);
        orderItemRepository.save(orderItems);
    }
}
