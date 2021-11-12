package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.TripEntity;
import com.apus.drones.apusdronesbackend.service.dto.OrderDTO;
import com.apus.drones.apusdronesbackend.service.dto.PilotDTO;
import com.apus.drones.apusdronesbackend.service.dto.TripDTO;

import java.util.Objects;
import java.util.Optional;

public class TripDTOMapper {

    public static TripDTO fromTripEntity(TripEntity tripEntity) {
        if (Objects.isNull(tripEntity)) {
            return null;
        }

        OrderDTO order = Optional.ofNullable(tripEntity.getOrder()).map(OrderDTOMapper::fromOrderEntity).orElse(null);
        PilotDTO pilot = Optional.ofNullable(tripEntity.getPilot()).map(PilotDTOMapper::fromPilotEntity).orElse(null);

        return TripDTO.builder()
                .id(tripEntity.getId())
                .pilot(pilot)
                .order(order)
                .createdAt(tripEntity.getCreatedAt())
                .collectedAt(tripEntity.getCollectedAt())
                .deliveredAt(tripEntity.getDeliveredAt())
                .build();
    }
}
