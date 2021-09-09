package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;

public class PartnerDtoMapper {

    public static PartnerDTO fromUserEntity(UserEntity partner) {
        return PartnerDTO
                .builder()
                .id(partner.getId())
                .name(partner.getName())
                .avatarUrl(partner.getAvatarUrl())
                .build();
    }
}
