package com.apus.drones.apusdronesbackend.service.dto;

import lombok.*;

import java.util.List;

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

    private List<AddressDTO> addresses;
}
