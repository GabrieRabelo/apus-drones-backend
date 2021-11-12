package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.*;

import java.util.List;

public interface PartnerService {

    List<PartnerDTO> findAllPartners();
    PartnerDTO get(Long id);
    CreatePartnerResponseDTO create(CreatePartnerDTO createPartnerDTO);
    PartnerDTO update(CreatePartnerDTO updatePartnerDTO);
    void delete();
    PartnerStatusDTO changeApprovalStatus(Long partnerId, PartnerApprovedDTO partnerApprovedDTO);
}
