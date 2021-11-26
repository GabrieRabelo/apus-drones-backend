package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper;
import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.PartnerStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.AddressRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AddressRepository addressRepository;
    private final PointCreatorService pointCreatorService;

    public PartnerServiceImpl(UserRepository userRepository, AddressRepository addressRepository, PointCreatorService pointCreatorService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.pointCreatorService = pointCreatorService;
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
        final var userEntityToSave = UserEntity.builder()
                .name(createPartnerDTO.getName())
                .email(createPartnerDTO.getEmail())
                .avatarUrl(createPartnerDTO.getAvatarUrl())
                .cpfCnpj(createPartnerDTO.getCpfCnpj())
                .password(createPartnerDTO.getPassword())
                .deleted(Boolean.FALSE)
                .role(Role.PARTNER)
                .build();

        final var savedUserEntity = userRepository.save(userEntityToSave);

        final var userAddress = createPartnerDTO.getAddress();

        final var coords = pointCreatorService.createPoint(userAddress.getLng(), userAddress.getLat());

        final var address = AddressEntity.builder()
                .description(userAddress.getDescription())
                .number(userAddress.getNumber())
                .coordinates(coords)
                .user(savedUserEntity)
                .zipCode(userAddress.getZipCode())
                .build();

        final var savedAddress = addressRepository.save(address);

        log.info("Saved new user entity: {}", savedUserEntity);
        log.info("Saved new user address: {}", savedAddress);

        CreatePartnerResponseDTO response = new CreatePartnerResponseDTO();
        response.setId(savedUserEntity.getId());

        return response;
    }

    @Override
    public PartnerDTO update(CreatePartnerDTO updatePartnerDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            UserEntity entity = userRepository.findAllByIdAndRole(details.getUserID(), Role.PARTNER)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Não foi possível encontrar o Parceiro com ID " + details.getUserID()));

            updatePartner(updatePartnerDTO, entity);

            UserEntity savedUserEntity = userRepository.save(entity);

            return PartnerDtoMapper.fromUserEntity(savedUserEntity);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
    }

    private void updatePartner(CreatePartnerDTO partnerDTO, UserEntity entity) {
        if (partnerDTO.getName() != null)
            entity.setName(partnerDTO.getName());

        if (partnerDTO.getEmail() != null)
            entity.setEmail(partnerDTO.getEmail());

        // TODO: Should it throw error or just dont update?
        if (partnerDTO.getCpfCnpj() != null) {
            entity.setCpfCnpj(partnerDTO.getCpfCnpj());
        }

        if (partnerDTO.getAvatarUrl() != null)
            entity.setAvatarUrl(partnerDTO.getAvatarUrl());

        if (partnerDTO.getPassword() != null)
            entity.setPassword(partnerDTO.getPassword());
    }

    @Override
    public void delete() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            UserEntity entity = userRepository.findByIdAndRoleAndDeletedFalse(details.getUserID(), Role.PARTNER)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Não foi possível encontrar o Parceiro com ID " + details.getUserID()));

            entity.setDeleted(Boolean.TRUE);

            userRepository.save(entity);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
    }

    @Override
    public PartnerStatusDTO changeApprovalStatus(Long partnerId, PartnerApprovedDTO partnerApprovedDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

            boolean isAdmin = details.getRole() == Role.ADMIN;

            if (isAdmin) {
                UserEntity entity =
                        userRepository.findById(partnerId)
                                .orElseThrow();

                if (entity.getRole() == Role.PARTNER) {
                    entity.setStatus(partnerApprovedDTO.isApproved() ? PartnerStatus.APPROVED : PartnerStatus.REJECTED);

                    userRepository.save(entity);
                    return PartnerStatusDTO.builder().status(entity.getStatus()).build();
                }

                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "O usuário encontrado não é um parceiro");

            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "O usuário não possui privilégios para aprovar/desaprovar parceiros");
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
    }
}
