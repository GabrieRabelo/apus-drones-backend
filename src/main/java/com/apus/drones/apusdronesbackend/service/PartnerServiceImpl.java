package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerResponseDTO;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Transactional
@Service
@Slf4j
public class PartnerServiceImpl implements PartnerService {

    private final UserRepository userRepository;

    public PartnerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<PartnerDTO> findAllPartners() {
        var resultFromDB = userRepository.findAllByRole(Role.PARTNER);

        return PartnerDtoMapper.fromUserEntityList(resultFromDB);
    }

    @Override
    public CreatePartnerResponseDTO create(CreatePartnerDTO createPartnerDTO) {
        UserEntity userEntityToSave = UserEntity.builder()
                .name(createPartnerDTO.getName())
                .email(createPartnerDTO.getEmail())
                .avatarUrl(createPartnerDTO.getAvatarUrl())
                .cpfCnpj(createPartnerDTO.getCpfCnpj())
                .password(createPartnerDTO.getPassword())
                .role(Role.PARTNER)
                .build();

        UserEntity savedUserEntity = userRepository.save(userEntityToSave);

        log.info("Saved new user entity with id [{}]", savedUserEntity.getId());

        CreatePartnerResponseDTO response = new CreatePartnerResponseDTO();
        response.setId(savedUserEntity.getId());

        return response;
    }

}
