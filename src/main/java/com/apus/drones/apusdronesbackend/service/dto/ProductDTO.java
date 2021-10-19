package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private Double weight;
    private LocalDateTime createdAt;
    private String imageUrl;
    private List<String> imagesUrls;
    private PartnerDTO partner;
    private Integer quantity;
    private Boolean deleted;
}
