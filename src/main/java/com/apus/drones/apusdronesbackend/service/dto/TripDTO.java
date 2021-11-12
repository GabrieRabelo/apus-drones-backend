package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TripDTO {
    private Long id;

    private PilotDTO pilot;

    private OrderDTO order;

    private LocalDateTime createdAt;

    private LocalDateTime collectedAt;

    private LocalDateTime deliveredAt;
}
