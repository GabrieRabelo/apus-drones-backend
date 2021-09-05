package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.PartnerDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void testFindPartners() {

        var user1 = UserEntity.builder()
                .id(1L)
                .avatarUrl("www.static-img.com/dummy.jpg")
                .name("Mister X")
                .build();
        var userList = List.of(user1);

        when(userRepository.findAllByRole(any())).thenReturn(userList);

        var result = partnerService.findAllPartners();

        var partner = PartnerDTO.builder().id(1L).avatarUrl("www.static-img.com/dummy.jpg").name("Mister X").build();
        var expectedResult = List.of(partner);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(expectedResult);
    }
}
