package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Builder
@Getter
public class CreateCustomerDTO {
    private String name;
    @Pattern(regexp = "\\d{11}|\\d{14}", message = "Field cpfCnpj is not a valid CNPJ")
    private String cpfCnpj;
    private String email;
    private String password;
    private String avatarUrl;
    private String address;
}
