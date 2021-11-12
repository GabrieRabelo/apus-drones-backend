package com.apus.drones.apusdronesbackend.config;

import com.apus.drones.apusdronesbackend.model.entity.*;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.*;
import com.apus.drones.apusdronesbackend.service.PointCreatorService;
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

    private static int entityCount;
    private static final Integer NUMBER_OF_PARTNERS = 10;
    private static final Integer NUMBER_OF_PILOTS = 2;
    private static final Integer TIME_TO_REJECT_ORDER_MINUTES = 5;

    public final UserRepository userRepository;
    public final ProductRepository productRepository;
    public final ProductImageRepository productImageRepository;
    public final OrderRepository orderRepository;
    public final OrderItemRepository orderItemRepository;
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
        populatePilots();
    }

    private void initUsers() {
        List<UserEntity> usersToCreate = new ArrayList<>();

        var admin = UserEntity.builder()
                .name("Admin")
                .deleted(false)
                .role(Role.ADMIN)
                .password("APU2DR0N3")
                .email("apus.admin@example.com")
                .avatarUrl("")
                .build();
        usersToCreate.add(admin);

        usersToCreate.add(UserEntity.builder().name("Rabelo").role(Role.CUSTOMER)
                .avatarUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/User_with_smile.svg/1024px-User_with_smile.svg.png")
                .cpfCnpj("12312312312").password("blublu").email("rabelo@example.com").build());

        usersToCreate.add(UserEntity.builder().name("Carlos Alberto").role(Role.CUSTOMER).avatarUrl("https://static.poder360.com.br/2020/12/Apple-868x644.jpg")
                .cpfCnpj("40782976093").password("blublu").email("carlos.alberto@example.com").build());

        for (UserEntity userEntity : usersToCreate) {
            userRepository.save(userEntity);
            initAddress(userEntity);
        }
    }

    private void initAddress(UserEntity userEntity) {
        Double x, y;
        if (userEntity.getRole().equals(Role.CUSTOMER)) {
            x = -30.067167;
            y = -51.179395;
        } else {
            x = -30.0496352;
            y = -51.1689972;
        }

        var address = AddressEntity.builder()
                .number(123)
                .zipCode("123456")
                .complement("Complemento")
                .description("Rua Teste do user " + userEntity.getName())
                .user(userEntity)
                .coordinates(new PointCreatorService().createPoint(x, y))
                .build();

        addressRepository.save(address);
    }

    private void initOrders() {
        for (int i = 0; i < NUMBER_OF_PARTNERS; i++) {
            populateOrders(i);
        }

//        for (int i = 0; i < 2; i++) {
//            populateCart(i);
//        }
    }

    private void populateCart(Integer customerIndex) {
        OrderEntity orderToCreate = OrderEntity.builder().customer(userRepository.findAllByRole(Role.CUSTOMER).get(customerIndex))
                .partner(userRepository.findAllByRole(Role.PARTNER).get(0))
                .status(OrderStatus.IN_CART)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .orderPrice(BigDecimal.ZERO).build();

        List<OrderItemEntity> orderItemsToCreate = new ArrayList<>();

        int quantity = 1;
        ProductEntity product = productRepository.findAll().get(0);
        orderItemsToCreate.add(OrderItemEntity.builder()
                .quantity(quantity)
                .price(product.getPrice())
                .order(orderToCreate).product(product)
                .weight(product.getWeight() * quantity)
                .build()
        );

        quantity = 2;
        product = productRepository.findAll().get(1);
        orderItemsToCreate.add(OrderItemEntity.builder()
                .quantity(quantity)
                .price(product.getPrice())
                .order(orderToCreate).product(product)
                .weight(product.getWeight() * quantity)
                .build()
        );

        BigDecimal orderPrice = orderItemsToCreate.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, (acc, item) -> acc.add(item));
        orderToCreate.setOrderPrice(orderPrice);

        orderRepository.save(orderToCreate);
        orderItemRepository.saveAll(orderItemsToCreate);
    }

    private void populateOrders(Integer partnerIndex) {
        List<OrderEntity> ordersToCreate = new ArrayList<>();
        var partner = userRepository.findAllByRole(Role.PARTNER).get(partnerIndex);
        var customer = userRepository.findAllByRole(Role.CUSTOMER).get(0);
        ordersToCreate.add(OrderEntity.builder().customer(customer)
                .partner(partner)
                .status(OrderStatus.WAITING_FOR_PILOT)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .orderPrice(new BigDecimal("100"))
                .deliveryAddress(addressRepository.findAllByUser_Id(partner.getId()).get(0))
                .shopAddress(addressRepository.findAllByUser_Id(customer.getId()).get(0))
                .build());

        ordersToCreate.add(OrderEntity.builder().customer(customer)
                .partner(partner)
                .status(OrderStatus.ACCEPTED)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .deliveryAddress(addressRepository.findAllByUser_Id(partner.getId()).get(0))
                .shopAddress(addressRepository.findAllByUser_Id(customer.getId()).get(0))
                .orderPrice(new BigDecimal("50")).build());

        ordersToCreate.add(OrderEntity.builder().customer(customer)
                .partner(partner)
                .status(OrderStatus.WAITING_FOR_PARTNER)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .deliveryAddress(addressRepository.findAllByUser_Id(partner.getId()).get(0))
                .shopAddress(addressRepository.findAllByUser_Id(customer.getId()).get(0))
                .orderPrice(new BigDecimal("225")).build());

        ordersToCreate.add(OrderEntity.builder().customer(customer)
                .partner(partner)
                .status(OrderStatus.WAITING_FOR_PARTNER)
                .expiresAt(LocalDateTime.now().plusMinutes(TIME_TO_REJECT_ORDER_MINUTES))
                .createdAt(LocalDateTime.now()).deliveryPrice(new BigDecimal("50"))
                .deliveryAddress(addressRepository.findAllByUser_Id(partner.getId()).get(0))
                .shopAddress(addressRepository.findAllByUser_Id(customer.getId()).get(0))
                .orderPrice(new BigDecimal("100")).build());

        List<OrderItemEntity> ordersItemToCreate = new ArrayList<>();

        var quantity = 2;

        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(50))
                .order(ordersToCreate.get(0)).product(productRepository.findAll().get(0))
                .weight(productRepository.findAll().get(0).getWeight() * quantity).build());


        quantity = 3;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(75))
                .order(ordersToCreate.get(1)).product(productRepository.findAll().get(2))
                .weight(productRepository.findAll().get(2).getWeight() * quantity).build());

        quantity = 1;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(10))
                .order(ordersToCreate.get(1)).product(productRepository.findAll().get(3))
                .weight(productRepository.findAll().get(1).getWeight() * quantity).build());

        quantity = 3;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(5))
                .order(ordersToCreate.get(1)).product(productRepository.findAll().get(4))
                .weight(productRepository.findAll().get(4).getWeight() * quantity).build());

        quantity = 4;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(25))
                .order(ordersToCreate.get(2)).product(productRepository.findAll().get(3))
                .weight(productRepository.findAll().get(3).getWeight() * quantity).build());

        quantity = 1;
        ordersItemToCreate.add(OrderItemEntity.builder().quantity(quantity).price(new BigDecimal(10))
                .order(ordersToCreate.get(2)).product(productRepository.findAll().get(4))
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
                            + entityCount + ".png")
                    .build();

            var product = ProductEntity.builder().user(user).weight(200.0).status(ProductStatus.ACTIVE)
                    .name("Produto " + entityCount).description("Lorem ipsum")
                    .price(BigDecimal.valueOf(new Random().nextInt(1000)))
                    .createDate(LocalDateTime.now()).productImages(List.of(productImage))
                    .quantity(25).deleted(Boolean.FALSE).build();

            productImage.setProduct(product);
            productRepository.save(product);
            entityCount++;
        }
    }

    private void populatePartners() {
        for (int i = 0; i < NUMBER_OF_PARTNERS; i++) {
            var user = UserEntity.builder().name("Parceiro " + entityCount).role(Role.PARTNER).avatarUrl(
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                                    + entityCount + ".png")
                    .cpfCnpj("12312312312").password("blublu")
                    .email("parceiro" + i + "@example.com")
                    .deleted(Boolean.FALSE).build();

            userRepository.save(user);
            initAddress(user);
            entityCount++;
            populateProducts(user.getId());
        }
    }

    private void populatePilots() {
        for (int i = 0; i < NUMBER_OF_PILOTS; i++) {
            var user = UserEntity.builder().name("Piloto " + entityCount).role(Role.PILOT).avatarUrl(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
                            + entityCount + ".png"
                    )
                    .cpfCnpj(String.format("%011d", new Random().nextInt(Integer.MAX_VALUE)))
                    .email(String.format("piloto%s@example.com", i))
                    .password("blublu")
                    .deleted(Boolean.FALSE)
                    .build();

            userRepository.save(user);
            initAddress(user);
            entityCount++;
        }
    }
}
