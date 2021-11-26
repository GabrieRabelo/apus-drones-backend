package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.AuthenticationService;
import com.apus.drones.apusdronesbackend.service.CustomerService;
import com.apus.drones.apusdronesbackend.service.PartnerService;
import com.apus.drones.apusdronesbackend.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
public class AuthenticationAPI {

    @Resource
    private AuthenticationService authenticationService;

    @Resource
    private final CustomerService customerService;

    @Resource
    private final PartnerService partnerService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        return new ResponseEntity<>(authenticationService.authenticate(jwtRequest), HttpStatus.OK);
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<CreateCustomerResponseDTO> create(@RequestBody @Validated CreateCustomerDTO createCustomerDTO) {
        return ResponseEntity.ok(customerService.create(createCustomerDTO));
    }

    @PostMapping("/signup/partner")
    public ResponseEntity<CreatePartnerResponseDTO> create(@RequestBody @Validated CreatePartnerDTO createPartnerDTO) {
        return ResponseEntity.ok(partnerService.create(createPartnerDTO));
    }
}
