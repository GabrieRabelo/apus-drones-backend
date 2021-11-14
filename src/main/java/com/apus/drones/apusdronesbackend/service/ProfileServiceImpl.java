package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.mapper.AddressDTOMapper;
import com.apus.drones.apusdronesbackend.mapper.UserDTOMapper;
import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.repository.AddressRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;
import com.apus.drones.apusdronesbackend.service.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PointCreatorService pointCreatorService;

    public ProfileServiceImpl(UserRepository userRepository, AddressRepository addressRepository,
                              PointCreatorService pointCreatorService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.pointCreatorService = pointCreatorService;
    }

    @Override
    public UserDTO getById() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            UserEntity entity = userRepository.getById(details.getUserID());
            List<AddressEntity> addresses = addressRepository.findAllByUser_Id(details.getUserID());

            List<AddressDTO> addressesDto =
                addresses.stream().map(AddressDTOMapper::fromAddressEntity).collect(Collectors.toList());

            return UserDTOMapper.fromUserEntity(entity, addressesDto);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Falha ao carregar dados do perfil. Usuário não autenticado.");
        }
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

            UserEntity entity = userRepository.findById(details.getUserID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));

            List<AddressDTO> addressDTOList = new ArrayList<>();
            updateUser(userDTO, entity, addressDTOList);
            UserEntity savedUserEntity = userRepository.save(entity);
            return UserDTOMapper.fromUserEntity(savedUserEntity, addressDTOList);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
    }

    private void updateUser(UserDTO userDTO, UserEntity entity, List<AddressDTO> addressDTOList) {
        if (userDTO.getName() != null) {
            entity.setName(userDTO.getName());
        }

        if (userDTO.getEmail() != null) {
            entity.setEmail(userDTO.getEmail());
        }

        if (userDTO.getCpfCnpj() != null) {
            entity.setCpfCnpj(userDTO.getCpfCnpj());
        }

        if (userDTO.getAvatarUrl() != null) {
            entity.setAvatarUrl(userDTO.getAvatarUrl());
        }

        if (userDTO.getAddresses() != null) {
            updateAddress(userDTO, addressDTOList);
        }
    }

    private void updateAddress(UserDTO userDTO, List<AddressDTO> addressDTOList) {
        userDTO.getAddresses().forEach(address -> {
            if (address.getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Para atualizar um endereço, é necessário inserir o ID do endereço.");
            }
            try {
                AddressEntity finalAddress = addressRepository.getById(address.getId());
                if (address.getDescription() != null) {
                    finalAddress.setDescription(address.getDescription());
                }
                if (address.getLat() != null || address.getLng() != null) {
                    finalAddress.setCoordinates(pointCreatorService.createPoint(
                        address.getLat() != null ? address.getLat() : finalAddress.getCoordinates().getX(),
                        address.getLng() != null ? address.getLng() : finalAddress.getCoordinates().getY()));
                }
                if (address.getNumber() != null) {
                    finalAddress.setNumber(address.getNumber());
                }
                addressRepository.save(finalAddress);
                addressDTOList.add(AddressDTOMapper.fromAddressEntity(finalAddress));
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Para atualizar um endereço, é necessário inserir um ID de endereço válido.");
            }
        });
    }
}
