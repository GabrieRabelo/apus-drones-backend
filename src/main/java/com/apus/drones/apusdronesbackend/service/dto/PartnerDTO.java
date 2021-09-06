package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PartnerDTO {
    private Long id;
    private String name;
    private String avatarUrl;
}
