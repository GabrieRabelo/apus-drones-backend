package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;

public class AddressDTOMapper {

    public static AddressDTO fromAddressEntity(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }
        return AddressDTO.builder()
                .id(addressEntity.getId())
                .description(addressEntity.getDescription())
                .number(addressEntity.getNumber())
                .lat(addressEntity.getCoordinates().getX())
                .lng(addressEntity.getCoordinates().getY())
                .build();
    }
}
