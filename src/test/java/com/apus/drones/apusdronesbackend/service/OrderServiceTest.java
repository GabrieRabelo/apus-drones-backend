package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.*;
import com.apus.drones.apusdronesbackend.service.dto.CustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AddressRepository addressRepository;

    @DisplayName("Should removed cart if emptied")
    @Test
    void testShouldRemoveCartWhenEmptied() {
        OrderService orderService = new OrderServiceImpl(
            orderRepository, orderItemRepository, userRepository, productRepository,
            "10", "2000.00", addressRepository
        );

        UserEntity customer = UserEntity
            .builder()
            .id(1L)
            .role(Role.CUSTOMER)
            .name("customer")
            .email("customer@customer.com")
            .cpfCnpj("1")
            .avatarUrl("none")
            .build();

        UserEntity partner = UserEntity
            .builder()
            .id(2L)
            .role(Role.PARTNER)
            .name("partner")
            .email("partner@partner.com")
            .cpfCnpj("2")
            .avatarUrl("none")
            .build();

        OrderEntity orderToUpdate = OrderEntity
            .builder()
            .id(1L)
            .orderPrice(BigDecimal.valueOf(10))
            .deliveryPrice(BigDecimal.valueOf(5))
            .status(OrderStatus.IN_CART)
            .customer(customer)
            .partner(partner)
            .build();

        OrderDTO orderUpdater = OrderDTO
            .builder()
            .createdAt(LocalDateTime.now())
            .orderPrice(BigDecimal.valueOf(10))
            .deliveryPrice(BigDecimal.valueOf(5))
            .customer(CustomerDTO.builder().id(1L).build())
            .partner(PartnerDTO.builder().id(1L).build())
            .id(1L)
            .status(OrderStatus.IN_CART)
            .items(Collections.emptyList())
            .build();

        AddressEntity address = AddressEntity.builder().build();

        when(userRepository.getById(customer.getId())).thenReturn(customer);
        when(addressRepository.findAllByUser_Id(Mockito.anyLong())).thenReturn(List.of(address));
        when(orderRepository.save(Mockito.any())).thenReturn(orderToUpdate);
        doNothing().when(orderRepository).delete(orderToUpdate);

        OrderDTO updatedOrder = orderService.update(orderUpdater);

        verify(orderRepository, times(1)).delete(orderToUpdate);
        assertThat(updatedOrder).isNull();
    }
}
