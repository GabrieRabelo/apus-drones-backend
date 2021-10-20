package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CreateProductDTO {
    @NotBlank(message = "Nome não pode ser nulo ou vazio")
    private String name;

    private String description;

    @NotNull(message = "Preço não pode ser nulo ou vazio")
    private BigDecimal price;

    @NotNull(message = "Status não pode ser nulo ou vazio")
    private ProductStatus status;

    @NotNull(message = "Peso não pode ser nulo ou vazio")
    private double weight;

    private List<FileDTO> files;

    private Integer quantity;

    private UserEntity partner;
}
