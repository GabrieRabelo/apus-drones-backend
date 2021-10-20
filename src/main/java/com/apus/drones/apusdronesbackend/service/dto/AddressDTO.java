package com.apus.drones.apusdronesbackend.service.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class AddressDTO {

    private Long id;

    private String description;

    private Integer number;

}
