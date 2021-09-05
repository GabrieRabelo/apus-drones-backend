package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllPartnerUsers() {
        var partner = UserEntity.builder()
                .name("Mister X")
                .avatarUrl("https://static-images.ifood.com.br/image/upload/t_high/logosgde/5ff52da2-464b-4934-af16-9dadec52201f/201807231152_mrxma.png")
                .role(Role.PARTNER)
                .build();

        var customer = UserEntity.builder()
                .name("Bigodao")
                .avatarUrl("https://static-images.ifood.com.br/image/upload/t_high/logosgde/5ff52da2-464b-4934-af16-9dadec52201f/201807231152_mrxma.png")
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(partner);
        userRepository.save(customer);
        userRepository.flush();

        var result = userRepository.findAllByRole(Role.PARTNER);
        var expectedResult = List.of(partner);

        assertThat(result)
                .usingElementComparatorIgnoringFields("id")
                .usingRecursiveFieldByFieldElementComparator()
                .hasSameElementsAs(expectedResult);
    }
}
