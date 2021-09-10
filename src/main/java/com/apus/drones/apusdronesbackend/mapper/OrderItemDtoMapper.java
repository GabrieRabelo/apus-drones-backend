package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.OrderItemEntity;
import com.apus.drones.apusdronesbackend.service.dto.OrderItemDto;

public class OrderItemDtoMapper {

    public static OrderItemDto fromOrderItemEntity(OrderItemEntity orderItemEntity) {
        return OrderItemDto.builder()
                .id(orderItemEntity.getId())
                .price(orderItemEntity.getPrice())
                .product(ProductDtoMapper.fromProductEntity(orderItemEntity.getProduct()))
                .quantity(orderItemEntity.getQuantity())
                .build();
    }
}
