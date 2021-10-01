package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.AddressDTOMapper;
import com.apus.drones.apusdronesbackend.mapper.UserDTOMapper;
import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.repository.AddressRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;
import com.apus.drones.apusdronesbackend.service.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }
    @Override
    public UserDTO getById(Long userId) {
        UserEntity entity = userRepository.getById(userId);
        List<AddressEntity> addresses = addressRepository.findAllByUser_Id(userId);

        List<AddressDTO> addressesDto = addresses.stream().map(AddressDTOMapper::fromAddressEntity).collect(Collectors.toList());

        return UserDTOMapper.fromUserEntity(entity, addressesDto);
    }
}
