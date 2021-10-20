package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.model.entity.*;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Profile({"local", "prd"})
@Configuration
public class Bootstrap {

    private static int contEntities;
    public final UserRepository userRepository;
    public final ProductRepository productRepository;
    public final ProductImageRepository productImageRepository;
    public final OrderRepository orderRepository;
    public final OrderItemRepository orderItemRepository;
    private static final Integer NUMBER_OF_PARTNERS = 10;
    private static final Integer TIME_TO_REJECT_ORDER_MINUTES = 5;
    public final AddressRepository addressRepository;

    public Bootstrap(UserRepository userRepository, ProductRepository productRepository,
                     ProductImageRepository productImageRepository, OrderRepository orderRepository,
                     AddressRepository addressRepository, OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Bean
    public void initDatabase() {
        initUsers();
        populatePartners();
        initOrders();
    }

    private void initUsers() {
        List<UserEntity> usersToCreate = new ArrayList<>();

        usersToCreate.add(UserEntity.builder().name("Rabelo").role(Role.CUSTOMER).avatarUrl("none")
                .cpfCnpj("12312312312").password("blublu").email("rabelo@example.com").build());

        usersToCreate.add(UserEntity.builder().name("Carlos Alberto").role(Role.CUSTOMER).avatarUrl("none")
                .cpfCnpj("40782976093").password("blublu").email("carlos.alberto@example.com").build());

        for (UserEntity userEntity : usersToCreate) {
            userRepository.save(userEntity);
            initAddress(userEntity);
            initAddress(userEntity);
        }
    }

    private void initAddress(UserEntity userEntity) {
        var address = AddressEntity.builder()
                .number(123)
                .zipCode("123456")
                .complement("Complemento")
                .description("Rua Teste do user " + userEntity.getName())
                .user(userEntity)
                .build();

        addressRepository.save(address);
    }

    private void initOrders() {
        for (int i = 0; i < NUMBER_OF_PARTNERS; i++) {
            populateOrders(i);
        }
    }

    private void populateOrders(Integer partnerIndex) {
        List<OrderEntity> ordersToCreate = new ArrayList<>();
        ordersToCreate.add(OrderEntity.builder().customer(userRepository.findAllByRole(Role.CUSTOMER).get(0))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(partnerIndex))
                .status(OrderStatus.ACCEPTED)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .orderPrice(new BigDecimal("100")).build());

        ordersToCreate.add(OrderEntity.builder().customer(userRepository.findAllByRole(Role.CUSTOMER).get(0))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(partnerIndex))
                .status(OrderStatus.IN_CART)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .orderPrice(new BigDecimal("50")).build());

        ordersToCreate.add(OrderEntity.builder().customer(userRepository.findAllByRole(Role.CUSTOMER).get(0))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(partnerIndex))
                .status(OrderStatus.WAITING_FOR_PARTNER)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .orderPrice(new BigDecimal("225")).build());

        ordersToCreate.add(OrderEntity.builder().customer(userRepository.findAllByRole(Role.CUSTOMER).get(1))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(partnerIndex))
                .status(OrderStatus.WAITING_FOR_PARTNER)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .orderPrice(new BigDecimal("100")).build());

        List<OrderItemEntity> ordersItemToCreate = new ArrayList<>();

        var quantity = 2;

        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(50))
                .order(ordersToCreate.get(0)).product(productRepository.findAll().get(0))
                .weight(productRepository.findAll().get(0).getWeight() * quantity).build());

        quantity = 1;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(50))
                .order(ordersToCreate.get(1)).product(productRepository.findAll().get(1))
                .weight(productRepository.findAll().get(1).getWeight() * quantity).build());

        quantity = 3;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(75))
                .order(ordersToCreate.get(2)).product(productRepository.findAll().get(2))
                .weight(productRepository.findAll().get(2).getWeight() * quantity).build());

        quantity = 1;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(10))
                .order(ordersToCreate.get(2)).product(productRepository.findAll().get(5))
                .weight(productRepository.findAll().get(5).getWeight() * quantity).build());

        quantity = 3;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(5))
                .order(ordersToCreate.get(2)).product(productRepository.findAll().get(4))
                .weight(productRepository.findAll().get(4).getWeight() * quantity).build());

        quantity = 4;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(25))
                .order(ordersToCreate.get(3)).product(productRepository.findAll().get(3))
                .weight(productRepository.findAll().get(3).getWeight() * quantity).build());

        quantity = 1;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(10))
                .order(ordersToCreate.get(3)).product(productRepository.findAll().get(4))
                .weight(productRepository.findAll().get(4).getWeight() * quantity).build());

        for (OrderEntity orderEntity : ordersToCreate) {
            orderRepository.save(orderEntity);
        }

        for (OrderItemEntity orderItemEntity : ordersItemToCreate) {
            orderItemRepository.save(orderItemEntity);
        }
    }

    private void populateProducts(long id) {
        for (int i = 0; i < 5; i++) {
            var user = userRepository.findById(id).orElse(null);
            var productImage = ProductImage.builder().isMain(true)
                    .url("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                            + contEntities + ".png")
                    .build();

            var product = ProductEntity.builder().user(user).weight(2D).status(ProductStatus.ACTIVE)
                    .name("Produto " + contEntities).description("Lorem ipsum")
                    .price(BigDecimal.valueOf(new Random().nextInt(1000)))
                    .createDate(LocalDateTime.now()).productImages(List.of(productImage))
                    .quantity(25).deleted(Boolean.FALSE).build();

            productImage.setProduct(product);
            productRepository.save(product);
            contEntities++;
        }
    }

    private void populatePartners() {
        for (int i = 0; i < NUMBER_OF_PARTNERS; i++) {
            var user = UserEntity.builder().name("Parceiro " + contEntities).role(Role.PARTNER).avatarUrl(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                            + contEntities + ".png")
                    .cpfCnpj("12312312312").password("blublu")
                    .email("parceiro" + i + "@example.com")
                    .deleted(Boolean.FALSE).build();
            userRepository.save(user);
            contEntities++;
            populateProducts(user.getId());
        }
    }
}
