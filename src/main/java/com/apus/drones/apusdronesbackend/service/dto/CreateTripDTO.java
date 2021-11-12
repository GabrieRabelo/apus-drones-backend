package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CreateTripDTO {
    private Long orderId;

    private LocalDateTime createdAt;

    private LocalDateTime collectedAt;

    private LocalDateTime deliveredAt;
}
