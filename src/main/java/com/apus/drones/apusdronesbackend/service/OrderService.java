package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getByCustomerId(Long userId, OrderStatus status);

    OrderDTO getById(Long orderId);
}
