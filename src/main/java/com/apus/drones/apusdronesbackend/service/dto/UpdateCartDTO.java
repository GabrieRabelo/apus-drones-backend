package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UpdateCartDTO {
    private PartnerDTO partner;

    private List<OrderItemDto> items;
}
