package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.PartnerDtoMapper;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {

    @InjectMocks
    private PartnerServiceImpl partnerService;
    @Mock
    private UserRepository userRepository;

    @DisplayName("When repository returns multiple results " +
            "should convert into response and return it")
    @Test
    void testFindPartners() {

        var user1 = UserEntity.builder()
                .id(1L)
                .avatarUrl("www.static-img.com/dummy.jpg")
                .name("Mister X")
                .build();
        var user2 = UserEntity.builder()
                .id(2L)
                .avatarUrl("www.static-img.com/dummy.jpg")
                .name("Cachorro do Bigode")
                .build();
        var userList = List.of(user1, user2);

        when(userRepository.findAllByRole(any())).thenReturn(userList);

        var result = partnerService.findAllPartners();

        var partner1 = PartnerDTO.builder().id(1L).avatarUrl("www.static-img.com/dummy.jpg").name("Mister X").build();
        var partner2 = PartnerDTO.builder().id(2L).avatarUrl("www.static-img.com/dummy.jpg").name("Cachorro do Bigode").build();
        var expectedResult = List.of(partner1, partner2);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(expectedResult);
    }

    @DisplayName("When repository returns no results " +
            "should return an empty list")
    @Test
    void testFindPartners_emptyResult() {

        when(userRepository.findAllByRole(any())).thenReturn(List.of());

        var result = partnerService.findAllPartners();

        var expectedResult = List.of();

        assertThat(result).isEqualToComparingFieldByFieldRecursively(expectedResult);
    }

    private List<PartnerDTO> toDTOList(List<UserEntity> resultFromDB) {
        var responseList = new ArrayList<PartnerDTO>();

        for (UserEntity user: resultFromDB) {
            var partner = PartnerDTO
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .cpfCnpj(user.getCpfCnpj())
                .avatarUrl(user.getAvatarUrl())
                .build();

            responseList.add(partner);
        }

        return responseList;
    }
}
