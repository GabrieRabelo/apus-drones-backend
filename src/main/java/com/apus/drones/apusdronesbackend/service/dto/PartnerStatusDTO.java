package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.enums.PartnerStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartnerStatusDTO {
    public PartnerStatus status;
}
