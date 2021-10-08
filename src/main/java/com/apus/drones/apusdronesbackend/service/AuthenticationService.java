package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.service.dto.JwtRequest;
import com.apus.drones.apusdronesbackend.service.dto.JwtResponse;

public interface AuthenticationService {
    JwtResponse authenticate(JwtRequest jwtRequest) throws Exception;
}
