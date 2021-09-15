package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderResponse> findAllByCustomerId(Long userId);

    OrderResponse findById(Long orderId);

    OrderResponse update(OrderResponse orderResponse);
}
