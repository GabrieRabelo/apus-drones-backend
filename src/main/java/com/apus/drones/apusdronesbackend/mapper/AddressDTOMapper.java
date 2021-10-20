package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;

public class AddressDTOMapper {

    public static AddressDTO fromAddressEntity(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }
        var addressBuilder = AddressDTO.builder()
                .id(addressEntity.getId())
                .description(addressEntity.getDescription())
                .number(addressEntity.getNumber());

        if (addressEntity.getCoordinates() != null) {
            addressBuilder
                    .lat(addressEntity.getCoordinates().getX())
                    .lng(addressEntity.getCoordinates().getY());
        }

        return addressBuilder.build();
    }
}
