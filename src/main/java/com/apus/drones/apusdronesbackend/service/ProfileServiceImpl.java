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

    public ProfileServiceImpl(UserRepository userRepository, AddressRepository addressRepository, PointCreatorService pointCreatorService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.pointCreatorService = pointCreatorService;
    }
    @Override
    public UserDTO getById() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.isAuthenticated()){
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            UserEntity entity = userRepository.getById(details.getUserID());
            AddressEntity address = addressRepository.findByUser_Id(details.getUserID());

            AddressDTO addressDTO = AddressDTOMapper.fromAddressEntity(address);

            return UserDTOMapper.fromUserEntity(entity, addressDTO);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falha ao carregar dados do perfil. Usuário não autenticado.");
        }
    }

    @Override
    public UserDTO update (UserDTO userDTO){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();

            UserEntity entity = userRepository.findById(details.getUserID()).orElse(null);

            updateUser(userDTO, entity);
            UserEntity savedUserEntity = userRepository.save(entity);

            return UserDTOMapper.fromUserEntity(savedUserEntity, userDTO.getAddress());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
    }

    private void updateUser(UserDTO userDTO, UserEntity entity) {
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

        if (userDTO.getAddress() != null) {
            updateAddress(userDTO.getAddress(), userDTO.getId());
        }
    }

    private void updateAddress (AddressDTO addressDTO, Long userId) {

        try {
            AddressEntity finalAddress = addressRepository.getById(userId);
            if(addressDTO.getDescription() != null){
                finalAddress.setDescription(addressDTO.getDescription());
            }
            if(addressDTO.getLat() != null || addressDTO.getLng() != null){
                finalAddress.setCoordinates(pointCreatorService.createPoint(addressDTO.getLat() != null ? addressDTO.getLat() : finalAddress.getCoordinates().getX(),
                        addressDTO.getLng() != null ? addressDTO.getLng() : finalAddress.getCoordinates().getY()));
            }
            if(addressDTO.getNumber() != null){
                finalAddress.setNumber(addressDTO.getNumber());
            }
            addressRepository.save(finalAddress);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para atualizar um endereço, é necessário inserir um ID de endereço válido.");
        }
    }
}
