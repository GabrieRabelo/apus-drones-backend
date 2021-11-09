package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PartnerDTO {
    private Long id;
    private String name;
    private String email;
    private String cpfCnpj;
    private String avatarUrl;
    private Boolean deleted;
    private boolean approved;
}
