package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class OrderItemDto {

    private Long id;

    private Integer quantity;

    private BigDecimal price;

    private ProductDTO product;

    private Double weight;

}
