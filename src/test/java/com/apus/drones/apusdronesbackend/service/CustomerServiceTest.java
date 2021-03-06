package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.repository.AddressRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.AddressDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerResponseDTO;
import com.apus.drones.apusdronesbackend.service.dto.JwtResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
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
    @Mock
    private AuthenticationService authenticationService;

    @Test
    void create() {
        //Given
        final var address = AddressDTO.builder()
            .description("Av Ipiranga")
            .number(6681)
            .lat(-30.0611631)
            .lng(-51.1761906)
            .build();

        final var request = CreateCustomerDTO.builder()
            .name("Gabriel Rabelo")
            .address(address)
            .cpfCnpj("12312312312")
            .email("rabelo@rab.elo")
            .build();

        var response = UserEntity.builder().id(1L).build();

        //When
        when(userRepository.save(any())).thenReturn(response);
        when(addressRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        try {
            when(authenticationService.authenticate(any())).thenReturn(new JwtResponse("token"));
        } catch (Exception e) {
            fail("Falha ao gerar token");
        }

        var result = customerService.create(request);

        //Then
        var expectedResult = new CreateCustomerResponseDTO(1L, "token");

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }
}