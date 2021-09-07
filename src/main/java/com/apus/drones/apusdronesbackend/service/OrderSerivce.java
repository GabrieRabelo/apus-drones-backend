package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;

import java.util.List;

public interface OrderSerivce {
    List<OrderEntity> findAllByCustomerId(Long userId);
}
