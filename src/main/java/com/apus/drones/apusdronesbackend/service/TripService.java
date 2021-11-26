package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.CreateTripDTO;
import com.apus.drones.apusdronesbackend.service.dto.TripDTO;

import java.util.List;

public interface TripService {
    List<TripDTO> getByPilot();

    TripDTO get(Long tripId);

    TripDTO create(CreateTripDTO createTripDTO);

    TripDTO setCollected(Long tripId);

    TripDTO setDelivered(Long tripId);
}
