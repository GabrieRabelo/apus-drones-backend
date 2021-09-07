package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllOrdersByCustomer() {
        var customer = UserEntity.builder()
                .email("customer@gmail.com")
                .password("coxinha123")
                .cpfCnpj("12312312333")
                .name("Jorge")
                .role(Role.CUSTOMER)
                .build();

        var savedCustomer = userRepository.saveAndFlush(customer);

        var partner = UserEntity.builder()
                .name("Mister X")
                .avatarUrl("https://static-images.ifood.com.br/image/upload/t_high/logosgde/5ff52da2-464b-4934-af16-9dadec52201f/201807231152_mrxma.png")
                .role(Role.PARTNER)
                .build();

        var savedPartner = userRepository.saveAndFlush(partner);

        var order = OrderEntity.builder()
                .customer(savedCustomer)
                .partner(savedPartner)
                .status("Pendente")
                .deliveryPrice(new BigDecimal("50.00"))
                .build();

        orderRepository.saveAndFlush(order);

        var result = orderRepository.findAllByCustomer_Id(savedCustomer.getId()).get(0);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(order);
    }

}
