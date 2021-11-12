package com.apus.drones.apusdronesbackend.service.dto;

import com.apus.drones.apusdronesbackend.model.enums.PartnerStatus;
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
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;
    private PartnerStatus status;
}
