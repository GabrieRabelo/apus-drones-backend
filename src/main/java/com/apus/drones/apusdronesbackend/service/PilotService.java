package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.PilotDTO;

import java.util.List;

public interface PilotService {
    List<PilotDTO> findAllPilots();

    PilotDTO get(Long id);
}
