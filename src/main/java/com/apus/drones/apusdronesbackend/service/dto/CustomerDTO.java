package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String cpfCnpj;
    private String avatarUrl;
}
