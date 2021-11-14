package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;
import com.apus.drones.apusdronesbackend.service.dto.UserDTO;

import java.util.List;

public class UserDTOMapper {

    public static UserDTO fromUserEntity(UserEntity userEntity, List<AddressDTO> addresses) {

        return UserDTO.builder()
            .avatarUrl(userEntity.getAvatarUrl())
            .cpfCnpj(userEntity.getCpfCnpj())
            .email(userEntity.getEmail())
            .name(userEntity.getName())
            .addresses(addresses)
            .id(userEntity.getId())
            .build();
    }
}
