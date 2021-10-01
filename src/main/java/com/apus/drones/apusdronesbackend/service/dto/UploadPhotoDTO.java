package com.apus.drones.apusdronesbackend.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public final class UploadPhotoDTO {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UploadPhotoDTO(String fileName, String base64) {
        this.fileName = fileName;
        this.base64 = base64;
    }

    @NotBlank(message = "fileName não pode ser nulo ou vazio")
    private final String fileName;

    @NotBlank(message = "base64 não pode ser nulo ou vazio")
    private final String base64;
}
