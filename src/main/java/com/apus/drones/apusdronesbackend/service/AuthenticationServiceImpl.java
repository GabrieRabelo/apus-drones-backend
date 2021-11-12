package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.PartnerStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.JwtRequest;
import com.apus.drones.apusdronesbackend.service.dto.JwtResponse;
import com.apus.drones.apusdronesbackend.utils.JWTUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    private final UserRepository userRepository;

    @Override
    public JwtResponse authenticate(JwtRequest jwtRequest) throws Exception {
        UserEntity userEntity = userRepository.findByEmail(jwtRequest.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmail(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getEmail());
        if (userEntity.getRole().equals(Role.PARTNER) && !userEntity.getStatus().equals(PartnerStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seu cadastro ainda não foi aprovado");
        }

        final String token = jwtUtility.generateToken(userDetails, userEntity);

        return new JwtResponse(token);
    }
}