package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.CustomerService;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerDTO;
import com.apus.drones.apusdronesbackend.service.dto.CreateCustomerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerAPI {

    private final CustomerService customerService;

    public CustomerAPI(CustomerService clientService) {
        this.customerService = clientService;
    }

    @PostMapping
    public ResponseEntity<CreateCustomerResponseDTO> create(
            @RequestBody @Validated CreateCustomerDTO createCustomerDTO
    ) {
        return ResponseEntity.ok(customerService.create(createCustomerDTO));
    }

}
