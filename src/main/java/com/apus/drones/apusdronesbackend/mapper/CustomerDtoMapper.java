package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.service.dto.CustomerDTO;

public class CustomerDtoMapper {

    public static CustomerDTO fromUserEntity(UserEntity customer) {
        return CustomerDTO
            .builder()
            .id(customer.getId())
            .name(customer.getName())
            .avatarUrl(customer.getAvatarUrl())
            .cpfCnpj(customer.getCpfCnpj())
            .email(customer.getEmail())
            .build();
    }
}
