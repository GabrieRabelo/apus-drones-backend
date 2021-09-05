package com.apus.drones.apusdronesbackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDTO {
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
