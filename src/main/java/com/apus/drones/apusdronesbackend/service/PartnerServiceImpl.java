package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {

    private final UserRepository userRepository;

    public PartnerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<PartnerDTO> findAllPartners() {
        var resultFromDB = userRepository.findAllByRole(Role.PARTNER);
        var responseList = new ArrayList<PartnerDTO>();

        for (UserEntity user: resultFromDB) {
            var partner = PartnerDTO
                    .builder()
                    .id(user.getId())
                    .name(user.getName())
                    .avatarUrl(user.getAvatarUrl())
                    .build();

            responseList.add(partner);
        }

        return responseList;
    }
}
