package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.service.dto.PilotDTO;

public class PilotDTOMapper {

    public static PilotDTO fromPilotEntity(UserEntity pilot) {
        return PilotDTO.builder()
                .id(pilot.getId())
                .name(pilot.getName())
                .avatarUrl(pilot.getAvatarUrl())
                .cpfCnpj(pilot.getCpfCnpj())
                .email(pilot.getEmail())
                .build();
    }
}
