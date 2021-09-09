package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderSerivce {
    List<OrderEntity> findAllByCustomerId(Long userId);

    Optional<OrderEntity> findById(Long orderId);
}
