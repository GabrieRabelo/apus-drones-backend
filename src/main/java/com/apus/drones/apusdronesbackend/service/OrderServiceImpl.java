package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.OrderResponseMapper;
import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.model.entity.OrderItemEntity;
import com.apus.drones.apusdronesbackend.model.response.OrderResponse;
import com.apus.drones.apusdronesbackend.repository.OrderItemRepository;
import com.apus.drones.apusdronesbackend.repository.OrderRepository;
import com.apus.drones.apusdronesbackend.service.dto.OrderItemDto;
import org.hibernate.criterion.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal DEFAULT_DELIVERY_PRICE = BigDecimal.TEN;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderResponse> findAllByCustomerId(Long userId) {
        List<OrderEntity> orders = orderRepository.findAllByCustomer_Id(userId);
        for (OrderEntity o : orders) {
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

    @Override
    public OrderResponse update(OrderResponse orderResponse) {
        this.updateItems(orderResponse.getItems());
        this.updateOrder(orderResponse);

        return this.findById(orderResponse.getId());
    }

    private void updateOrder(OrderResponse orderResponse) {
        OrderEntity entity = OrderEntity.builder()
                .id(orderResponse.getId())
                .deliveryPrice(DEFAULT_DELIVERY_PRICE)
                .orderPrice(this.calcOrderPrice(orderResponse.getItems()))
                .status(orderResponse.getStatus())
                .build();

        orderRepository.save(entity);
    }

    private void updateItems(List<OrderItemDto> items) {
        items.stream()
                .filter(item -> item.getQuantity() < 1)
                .forEach(item -> orderItemRepository.deleteById(item.getId()));

        items.stream()
                .filter(item -> item.getQuantity() >= 1)
                .forEach(item -> {
                    OrderItemEntity entity = OrderItemEntity.builder()
                            .id(item.getId())
                            .quantity(item.getQuantity())
                            .build();

                    orderItemRepository.save(entity);
                });
    }

    private BigDecimal calcOrderPrice(List<OrderItemDto> items) {
        BigDecimal sum = BigDecimal.ZERO;

        for (OrderItemDto item : items) {
            sum = sum.add(item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        return sum;
    }
}
