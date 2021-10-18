package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getByCustomerId(Long userId, OrderStatus status);
    List<OrderDTO> findAllByPartnerIdAndFilterByStatus(Long userId, OrderStatus status);

    OrderDTO update(OrderDTO orderDto);

    OrderDTO getById(Long orderId);

    OrderDTO getCart(Long userId);
    void addToCart(Long userId, OrderDTO orderDTO);
}
