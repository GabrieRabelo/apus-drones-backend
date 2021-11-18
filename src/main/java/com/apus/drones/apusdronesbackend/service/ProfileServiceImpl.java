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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PointCreatorService pointCreatorService;

    public ProfileServiceImpl(UserRepository userRepository, AddressRepository addressRepository, PointCreatorService pointCreatorService) {
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

            List<AddressDTO> addressesDto = addresses.stream().map(AddressDTOMapper::fromAddressEntity).collect(Collectors.toList());

            return UserDTOMapper.fromUserEntity(entity, addressesDto);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falha ao carregar dados do perfil. Usuário não autenticado.");
        }
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

            UserEntity entity = userRepository.findById(details.getUserID()).orElse(null);

            List<AddressDTO> addressDTOList = new ArrayList<>();
            updateUser(userDTO, entity, addressDTOList);
            var savedUserEntity = userRepository.save(entity);
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
            updateAddress(userDTO, entity, addressDTOList);
        }
    }

    private void updateAddress(UserDTO userDTO, UserEntity userEntity, List<AddressDTO> addressDTOList) {

        var foundAddressList = addressRepository.findAllByUser_Id(userDTO.getId());
        if (!foundAddressList.isEmpty()) {
            var addr = foundAddressList.get(0);
            addr.setUser(null);
            addressRepository.save(addr);
        }

        final var userAddress = userDTO.getAddresses().get(0);

        final var coords = pointCreatorService.createPoint(userAddress.getLng(), userAddress.getLat());

        final var addressEntity = AddressEntity.builder()
                .description(userAddress.getDescription())
                .number(userAddress.getNumber())
                .coordinates(coords)
                .user(userEntity)
                .zipCode(userAddress.getZipCode())
                .build();

        var savedAddress = addressRepository.save(addressEntity);
        addressDTOList.add(AddressDTOMapper.fromAddressEntity(savedAddress));
    }
}
