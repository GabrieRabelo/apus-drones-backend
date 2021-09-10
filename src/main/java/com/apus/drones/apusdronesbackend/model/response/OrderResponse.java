package com.apus.drones.apusdronesbackend.model.response;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.dto.CustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.OrderItemDto;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderResponse {

    private Long id;

    private OrderStatus status;

    private List<OrderItemDto> items;

    private PartnerDTO partner;

    private CustomerDTO customer;

    private BigDecimal deliveryPrice;

    private BigDecimal orderPrice;

    private LocalDateTime createdAt;
}
