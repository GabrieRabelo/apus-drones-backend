package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class UpdateProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private Double weight;
    private Integer quantity;
    private List<FileDTO> files;
    private List<String> removedImagesUrls;
    private String mainImageUrl;
    private Boolean deleted;
}

