package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.service.S3UploadPhotoService;
import com.apus.drones.apusdronesbackend.service.dto.UploadPhotoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/photos")
@Slf4j
public class S3UploadPhotoAPI {
    private final S3UploadPhotoService s3UploadPhotoService;

    public S3UploadPhotoAPI(S3UploadPhotoService s3UploadPhotoService) {
        this.s3UploadPhotoService = s3UploadPhotoService;
    }

    @PostMapping
    private void uploadPhoto(@RequestBody @Validated UploadPhotoDTO request) {
        log.info("Received a new upload photo request");

        s3UploadPhotoService.upload(request);

        log.info("Photo uploaded successfully");
    }
}
