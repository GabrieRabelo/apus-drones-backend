package com.apus.drones.apusdronesbackend.mapper;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;

import java.util.ArrayList;
import java.util.List;

public class PartnerDtoMapper {

    public static PartnerDTO fromUserEntity(UserEntity partner) {
        return PartnerDTO
            .builder()
            .id(partner.getId())
            .email(partner.getEmail())
            .name(partner.getName())
            .cpfCnpj(partner.getCpfCnpj())
            .avatarUrl(partner.getAvatarUrl())
            .deleted(partner.getDeleted())
            .build();
    }

    public static List<PartnerDTO> fromUserEntityList(List<UserEntity> resultFromDB) {
        var responseList = new ArrayList<PartnerDTO>();

        for (UserEntity user : resultFromDB) {
            var partner = fromUserEntity(user);

            responseList.add(partner);
        }

        return responseList;
    }
}
