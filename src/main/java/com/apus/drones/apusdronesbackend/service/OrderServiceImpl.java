package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderSerivce {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderEntity> findAllByCustomerId(Long userId) {
        return orderRepository.findAllByCustomer_Id(userId);
    }
}
