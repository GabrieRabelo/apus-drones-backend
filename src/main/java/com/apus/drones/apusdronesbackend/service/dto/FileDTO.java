package com.apus.drones.apusdronesbackend.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class FileDTO {
    @NotBlank(message = "fileName não pode ser nulo ou vazio")
    private String fileName;

    @NotBlank(message = "base64 não pode ser nulo ou vazio")
    private String base64;

    @NotNull(message = "mainFile deve ser true ou false")
    private boolean mainFile;
}
