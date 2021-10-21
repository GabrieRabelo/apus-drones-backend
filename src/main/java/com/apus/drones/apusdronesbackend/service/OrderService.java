package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getByCustomerId(OrderStatus status);
    List<OrderDTO> findAllByPartnerIdAndFilterByStatus(OrderStatus status);

    OrderDTO update(OrderDTO orderDto);

    OrderDTO getById(Long orderId);

    void addToCart(OrderDTO orderDTO);
}
