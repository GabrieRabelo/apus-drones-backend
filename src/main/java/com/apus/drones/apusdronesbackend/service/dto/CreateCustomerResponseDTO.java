package com.apus.drones.apusdronesbackend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateCustomerResponseDTO {
    private Long id;
    private String jwtToken;
}
