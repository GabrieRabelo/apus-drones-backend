package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.AddressRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PointCreatorService pointCreatorService;

    public CustomerService(UserRepository userRepository, AddressRepository addressRepository, PointCreatorService pointCreatorService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.pointCreatorService = pointCreatorService;
    }

    public CreateCustomerResponseDTO create(CreateCustomerDTO createCustomerDTO) {

        final var userEntityToSave = UserEntity.builder()
                .name(createCustomerDTO.getName())
                .cpfCnpj(createCustomerDTO.getCpfCnpj())
                .email(createCustomerDTO.getEmail())
                .password(createCustomerDTO.getPassword())
                .avatarUrl(createCustomerDTO.getAvatarUrl())
                .role(Role.CUSTOMER)
                .deleted(Boolean.FALSE)
                .build();

        final var savedUserEntity = userRepository.save(userEntityToSave);

        final var userAddress = createCustomerDTO.getAddress();

        final var coords = pointCreatorService.createPoint(userAddress.getLng(), userAddress.getLat());

        final var address = AddressEntity.builder()
                .description(userAddress.getDescription())
                .number(userAddress.getNumber())
                .coordinates(coords)
                .user(savedUserEntity)
                .build();

        addressRepository.save(address);

        log.info("Saved new user entity with id [{}]", savedUserEntity.getId());

        return new CreateCustomerResponseDTO(savedUserEntity.getId());
    }


}
