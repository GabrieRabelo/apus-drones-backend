package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerResponseDTO;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;

import java.util.List;

public interface PartnerService {

    List<PartnerDTO> findAllPartners();
    PartnerDTO get();
    CreatePartnerResponseDTO create(CreatePartnerDTO createPartnerDTO);
    PartnerDTO update(CreatePartnerDTO updatePartnerDTO);
    void delete();
}
