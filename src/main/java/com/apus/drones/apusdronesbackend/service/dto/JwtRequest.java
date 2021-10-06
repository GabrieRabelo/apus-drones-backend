package com.apus.drones.apusdronesbackend.service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    private String email;

    private String password;
}
