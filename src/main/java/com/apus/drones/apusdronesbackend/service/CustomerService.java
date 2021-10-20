package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

    private final UserRepository userRepository;

    public CustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CreateCustomerResponseDTO create(CreateCustomerDTO createCustomerDTO) {
        UserEntity userEntityToSave = UserEntity.builder()
                .name(createCustomerDTO.getName())
                .cpfCnpj(createCustomerDTO.getCpfCnpj())
                .email(createCustomerDTO.getEmail())
                .password(createCustomerDTO.getPassword())
                .avatarUrl(createCustomerDTO.getAvatarUrl())
                .role(Role.CUSTOMER)
                .build();

        UserEntity savedUserEntity = userRepository.save(userEntityToSave);

        log.info("Saved new user entity with id [{}]", savedUserEntity.getId());

        CreateCustomerResponseDTO response = new CreateCustomerResponseDTO();
        response.setId(savedUserEntity.getId());

        return response;
    }



}
