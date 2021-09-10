package com.apus.drones.apusdronesbackend.model.request.product;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductRequest {
    @NotNull(message = "ID do produto não pode ser nulo ou vazio")
    private Long productId;

    private String name;
    private BigDecimal price;
    private ProductStatus status;
    private Double weight;
}
