package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.AuthenticationService;
import com.apus.drones.apusdronesbackend.service.dto.JwtRequest;
import com.apus.drones.apusdronesbackend.service.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
public class AuthenticationAPI {

    @Resource
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        return new ResponseEntity<>(authenticationService.authenticate(jwtRequest), HttpStatus.OK);
    }
}
