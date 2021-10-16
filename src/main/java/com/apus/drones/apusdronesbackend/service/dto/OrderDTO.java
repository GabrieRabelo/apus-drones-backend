package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class OrderDTO {
    private Long id;

    private CustomerDTO customer;

    private PartnerDTO partner;

    private OrderStatus status;

    private BigDecimal deliveryPrice;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private BigDecimal orderPrice;

    private AddressEntity deliveryAddress;

    private AddressEntity shopAddress;

    private List<OrderItemDto> items;
}
