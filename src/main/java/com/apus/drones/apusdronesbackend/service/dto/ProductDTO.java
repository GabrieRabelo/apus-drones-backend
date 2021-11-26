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
    private List<String> removedImagesUrls;
    private PartnerDTO partner;
    private Integer quantity;
    //files to upload
    private List<FileDTO> files;
    private boolean deleted;
}

