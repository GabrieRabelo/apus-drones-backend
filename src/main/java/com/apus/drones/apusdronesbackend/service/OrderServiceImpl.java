package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.OrderResponseMapper;
import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.OrderItemEntity;
import com.apus.drones.apusdronesbackend.model.response.OrderResponse;
import com.apus.drones.apusdronesbackend.repository.OrderItemRepository;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderResponse> findAllByCustomerId(Long userId) {
        List<OrderEntity> orders = orderRepository.findAllByCustomer_Id(userId);
        for(OrderEntity o : orders) {
            List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(o.getId());
            o.setOrderItems(items);
        }
        return orders.stream().map(OrderResponseMapper::fromOrderEntity).collect(Collectors.toList());
    }

    @Override
    public OrderResponse findById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(orderId);
        order.setOrderItems(items);

        return OrderResponseMapper.fromOrderEntity(order);
    }
}
