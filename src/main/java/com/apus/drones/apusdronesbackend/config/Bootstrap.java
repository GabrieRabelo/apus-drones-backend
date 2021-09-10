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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
public class Bootstrap {

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
        initProducts();
        initOrders();
    }

    private void initUsers() {
        var userPartner = UserEntity.builder()
                .name("Rabelo")
                .role(Role.PARTNER)
                .avatarUrl("none")
                .cpfCnpj("12312312312")
                .password("blublu")
                .email("rabelo@rab.elo")
                .build();

        var userCustomer = UserEntity.builder()
                .name("Rabelo")
                .role(Role.CUSTOMER)
                .avatarUrl("none")
                .cpfCnpj("12312312312")
                .password("blublu")
                .email("rabelo@rab.elo")
                .build();

        userRepository.save(userCustomer);
        userRepository.save(userPartner);

        populateUsers();
    }

    private void initProducts() {
        populateProducts();
        if (true)
            return;

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

    private void initOrders() {
        var order = OrderEntity.builder()
                .customer(userRepository.findAllByRole(Role.CUSTOMER).get(0))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(0)) //TODO
                .status(OrderStatus.ACCEPTED)
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
        orderItemRepository.save(orderItems);
    }

    private void populateProducts() {
        int cont = 0;
        for (int i = 1; i < 21; i++) {
            for (int j = 1; j < 21; j++) {
                cont++;
                String s = "" + i;
                var user = userRepository.findById(Long.parseLong(s)).orElse(null);
                var productImage = ProductImage.builder().isMain(true).url("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + (i * 20 + j) + ".png").build();

                var product = ProductEntity.builder()
                        .user(user)
                        .weight(2D)
                        .status(ProductStatus.ACTIVE)
                        .name("Produto " + cont)
                        .price(BigDecimal.valueOf(new Random().nextInt(1000)))
                        .createDate(LocalDateTime.now())
                        .productImages(List.of(productImage))
                        .build();

                productImage.setProduct(product);
                productRepository.save(product);
            }
            ;
        }
        ;
    }

    private void populateUsers() {
        for (int i = 1; i < 21; i++) {
            var user2 = UserEntity.builder()
                    .name("Parceiro " + i)
                    .role(Role.PARTNER)
                    .avatarUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + i + ".png")
                    .cpfCnpj("12312312312")
                    .password("blublu")
                    .email("rabelo@rab.elo")
                    .build();
            userRepository.save(user2);
        }
    }
}
