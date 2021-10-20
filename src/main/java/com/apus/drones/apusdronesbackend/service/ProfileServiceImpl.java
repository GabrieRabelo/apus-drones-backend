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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public ProfileServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }
    @Override
    public UserDTO getById() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth.isAuthenticated()){
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            UserEntity entity = userRepository.getById(details.getUserID());
            List<AddressEntity> addresses = addressRepository.findAllByUser_Id(details.getUserID());

            List<AddressDTO> addressesDto = addresses.stream().map(AddressDTOMapper::fromAddressEntity).collect(Collectors.toList());

            return UserDTOMapper.fromUserEntity(entity, addressesDto);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falha ao carregar dados do perfil. Usuário não autenticado.");
        }
    }
}
