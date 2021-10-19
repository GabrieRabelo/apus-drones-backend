package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerResponseDTO;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;

import java.util.List;

public interface PartnerService {

    List<PartnerDTO> findAllPartners();

    PartnerDTO get(Long id);

    CreatePartnerResponseDTO create(CreatePartnerDTO createPartnerDTO);

    PartnerDTO update(Long id, CreatePartnerDTO updatePartnerDTO);

    void delete(Long id);
}
