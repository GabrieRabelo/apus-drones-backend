package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import com.apus.drones.apusdronesbackend.service.dto.UpdateCartDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getByCustomerId(OrderStatus status);

    List<OrderDTO> findAllByPartnerIdAndFilterByStatus(OrderStatus status);

    List<OrderDTO> findAllWaitingForPilot();

    OrderDTO update(OrderDTO orderDto);

    OrderDTO getById(Long orderId);

    void addToCart(UpdateCartDTO updateCartDTO);

    OrderDTO getCart();

    OrderDTO createCart(UpdateCartDTO updateCartDTO, Long customerId);

}
