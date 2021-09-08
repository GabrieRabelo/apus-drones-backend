package com.apus.drones.apusdronesbackend.model.request.product;

import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Nome não pode ser nulo ou vazio")
    private String name;

    @NotNull(message = "Preço não pode ser nulo ou vazio")
    private BigDecimal price;

    @NotNull(message = "Status não pode ser nulo ou vazio")
    private ProductStatus status;

    @NotNull(message = "Peso não pode ser nulo ou vazio")
    private double weight;
}
