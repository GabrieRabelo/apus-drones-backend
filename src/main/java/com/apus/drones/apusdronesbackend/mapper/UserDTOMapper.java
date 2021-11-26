package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;
import com.apus.drones.apusdronesbackend.service.dto.UserDTO;

public class UserDTOMapper {

    public static UserDTO fromUserEntity(UserEntity userEntity, AddressDTO address) {

        return UserDTO.builder()
            .avatarUrl(userEntity.getAvatarUrl())
            .cpfCnpj(userEntity.getCpfCnpj())
            .email(userEntity.getEmail())
            .name(userEntity.getName())
            .address(address)
            .id(userEntity.getId())
            .build();
    }
}
