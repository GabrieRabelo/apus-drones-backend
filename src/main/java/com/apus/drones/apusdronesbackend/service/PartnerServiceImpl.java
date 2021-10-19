package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreatePartnerResponseDTO;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

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
    public PartnerDTO get(Long id) {
        return userRepository.findByIdAndRoleAndDeletedFalse(id, Role.PARTNER).map(PartnerDtoMapper::fromUserEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o Parceiro com ID " + id));
    }

    @Override
    public CreatePartnerResponseDTO create(CreatePartnerDTO createPartnerDTO) {
        UserEntity userEntityToSave = UserEntity.builder()
                .name(createPartnerDTO.getName())
                .email(createPartnerDTO.getEmail())
                .avatarUrl(createPartnerDTO.getAvatarUrl())
                .cpfCnpj(createPartnerDTO.getCpfCnpj())
                .password(createPartnerDTO.getPassword())
                .deleted(Boolean.FALSE)
                .role(Role.PARTNER)
                .build();

        UserEntity savedUserEntity = userRepository.save(userEntityToSave);

        log.info("Saved new user entity with id [{}]", savedUserEntity.getId());

        CreatePartnerResponseDTO response = new CreatePartnerResponseDTO();
        response.setId(savedUserEntity.getId());

        return response;
    }

    @Override
    public PartnerDTO update(Long id, CreatePartnerDTO updatePartnerDTO) {
        UserEntity entity = userRepository.findAllByIdAndRole(id, Role.PARTNER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o Parceiro com ID " + id));

        updatePartner(updatePartnerDTO, entity);

        UserEntity savedUserEntity = userRepository.save(entity);

        return PartnerDtoMapper.fromUserEntity(savedUserEntity);
    }

    private void updatePartner(CreatePartnerDTO partnerDTO, UserEntity entity) {
        if (partnerDTO.getName() != null) {
            entity.setName(partnerDTO.getName());
        }

        if (partnerDTO.getEmail() != null) {
            entity.setEmail(partnerDTO.getEmail());
        }

        // TODO: Should it throw error or just dont update?
        if (partnerDTO.getCpfCnpj() != null) {
            entity.setCpfCnpj(partnerDTO.getCpfCnpj());
        }

        if (partnerDTO.getAvatarUrl() != null) {
            entity.setAvatarUrl(partnerDTO.getAvatarUrl());
        }

        if (partnerDTO.getPassword() != null) {
            entity.setPassword(partnerDTO.getPassword());
        }
    }

    @Override
    public void delete(Long id) {
        UserEntity entity = userRepository.findByIdAndRoleAndDeletedFalse(id, Role.PARTNER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o Parceiro com ID " + id));

        entity.setDeleted(Boolean.TRUE);

        userRepository.save(entity);
    }
}
