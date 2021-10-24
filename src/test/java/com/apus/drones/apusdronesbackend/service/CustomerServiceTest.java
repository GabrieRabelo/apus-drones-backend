package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.repository.AddressRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private PointCreatorService pointCreatorService;

    @Test
    void create() {
        //Given
        var address = AddressDTO.builder()
                .description("Av Ipiranga")
                .number(6681)
                .lat(-30.0611631)
                .lng(-51.1761906)
                .build();
        var request = CreateCustomerDTO.builder()
                .name("Gabriel Rabelo")
                .address(address)
                .cpfCnpj("12312312312")
                .email("rabelo@rab.elo")
                .build();

        var response = UserEntity.builder().id(1L).build();

        //When
        when(userRepository.save(any())).thenReturn(response);
        when(addressRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        var result = customerService.create(request);

        //Then
        var expectedResult = new CreateCustomerResponseDTO(1L);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }
}