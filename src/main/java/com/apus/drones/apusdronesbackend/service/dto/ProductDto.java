package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class ProductDto {

    private Long id;

    private String name;

    private BigDecimal price;

    private ProductStatus status;

    private double weight;

    private LocalDateTime createDate;
}
