package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.OrderDTOMapper;
import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.OrderItemEntity;
import com.apus.drones.apusdronesbackend.model.enums.OrderStatus;
import com.apus.drones.apusdronesbackend.repository.OrderItemRepository;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
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
    public List<OrderDTO> getByCustomerId(Long userId, OrderStatus status) {
        var ignoredStatuses = List.of(OrderStatus.IN_CART);
        List<OrderEntity> orders = status == null
                ? orderRepository.findAllByCustomer_IdAndStatusIsNotIn(userId, ignoredStatuses)
                : orderRepository.findAllByCustomer_IdAndStatus(userId, status);

        for(OrderEntity o : orders) {
            List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(o.getId());
            o.setOrderItems(items);
        }
        return orders.stream().map(OrderDTOMapper::fromOrderEntity).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        List<OrderItemEntity> items = orderItemRepository.findAllByOrder_Id(orderId);
        order.setOrderItems(items);

        return OrderDTOMapper.fromOrderEntity(order);
    }
}
