package com.apus.drones.apusdronesbackend.repository;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testFindAllUsers(){
        var userEntity = UserEntity.builder()
                .id(1L)
                .name("Mister X")
                .avatarUrl("https://static-images.ifood.com.br/image/upload/t_high/logosgde/5ff52da2-464b-4934-af16-9dadec52201f/201807231152_mrxma.png")
                .build();

        userRepository.save(userEntity);

        var result = userRepository.findAll().get(0);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(userEntity);
    }
}
