package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.converter.PartnerConverter;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {

    private final UserRepository userRepository;
    private final PartnerConverter partnerConverter;

    public PartnerServiceImpl(UserRepository userRepository, PartnerConverter partnerConverter) {
        this.userRepository = userRepository;
        this.partnerConverter = partnerConverter;
    }

    public List<PartnerDTO> findAllPartners() {
        var resultFromDB = userRepository.findAllByRole(Role.PARTNER);
        return partnerConverter.toDTOList(resultFromDB);
    }

}
