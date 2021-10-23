package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.OrderEntity;
import com.apus.drones.apusdronesbackend.service.dto.CustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import com.apus.drones.apusdronesbackend.service.dto.OrderItemDto;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderDTOMapper {

    public static OrderDTO fromOrderEntity(OrderEntity orderEntity) {
        if (Objects.isNull(orderEntity)) {
            return null;
        }

        List<OrderItemDto> orderItemDtoList = Optional.ofNullable(orderEntity.getOrderItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(OrderItemDtoMapper::fromOrderItemEntity)
                .collect(Collectors.toList());

        PartnerDTO partner = Optional.ofNullable(orderEntity.getPartner()).map(PartnerDtoMapper::fromUserEntity).orElse(null);
        CustomerDTO customer = Optional.ofNullable(orderEntity.getCustomer()).map(CustomerDtoMapper::fromUserEntity).orElse(null);

        return OrderDTO.builder()
                .id(orderEntity.getId())
                .createdAt(orderEntity.getCreatedAt())
                .deliveryPrice(orderEntity.getDeliveryPrice() == null ? BigDecimal.valueOf(0) : orderEntity.getDeliveryPrice())
                .items(orderItemDtoList)
                .orderPrice(orderEntity.getOrderPrice() == null ? BigDecimal.valueOf(0) : orderEntity.getOrderPrice())
                .partner(partner)
                .customer(customer)
                .expiresAt(orderEntity.getExpiresAt())
                .status(orderEntity.getStatus())
                .deliveryAddress(AddressDTOMapper.fromAddressEntity(orderEntity.getDeliveryAddress()))
                .shopAddress(AddressDTOMapper.fromAddressEntity(orderEntity.getShopAddress()))
                .build();

    }
}
