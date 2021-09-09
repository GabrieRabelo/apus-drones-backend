package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
