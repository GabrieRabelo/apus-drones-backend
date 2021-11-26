package com.apus.drones.apusdronesbackend.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class UserDTO {

    private Long id;

    private String email;

    private String cpfCnpj;

    private String name;

    private String avatarUrl;

    private AddressDTO address;
}
