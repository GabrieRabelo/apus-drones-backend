package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.AddressEntity;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.AddressRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerResponseDTO;
import com.apus.drones.apusdronesbackend.service.dto.JwtRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PointCreatorService pointCreatorService;
    private final AuthenticationService authenticationService;

    public CustomerService(UserRepository userRepository, AddressRepository addressRepository,
                           PointCreatorService pointCreatorService, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.pointCreatorService = pointCreatorService;
        this.authenticationService = authenticationService;
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
            .zipCode(userAddress.getZipCode())
            .build();

        final var savedAddress = addressRepository.save(address);

        var token = "";
        try {
            final var jwtResponse = this.authenticationService.authenticate(
                new JwtRequest(createCustomerDTO.getEmail(), createCustomerDTO.getPassword()));
            token = jwtResponse.getJwtToken();
        } catch (Exception e) {
            System.out.println("It was not possible to generate a JWT Token");
        }

        log.info("Saved new user entity: {}", savedUserEntity);
        log.info("Saved new user address: {}", savedAddress);

        return new CreateCustomerResponseDTO(savedUserEntity.getId(), token);
    }


}
