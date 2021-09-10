package com.apus.drones.apusdronesbackend.service.converter;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PartnerConverter {

    public List<PartnerDTO> toDTOList(List<UserEntity> resultFromDB) {
        var responseList = new ArrayList<PartnerDTO>();

        for (UserEntity user: resultFromDB) {
            var partner = PartnerDTO
                    .builder()
                    .id(user.getId())
                    .name(user.getName())
                    .avatarUrl(user.getAvatarUrl())
                    .build();

            responseList.add(partner);
        }

        return responseList;
    }
}
