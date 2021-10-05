package com.apus.drones.apusdronesbackend.api;

import com.apus.drones.apusdronesbackend.config.AmazonS3Config;
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
    private final AmazonS3Config config;

    public S3UploadPhotoAPI(AmazonS3Config config) {
        this.config = config;
    }

    @PostMapping
    private void uploadPhoto(@RequestBody @Validated UploadPhotoDTO request) {
        log.info("Received a new upload photo request");

        config.upload(request);

        log.info("Photo uploaded successfully");
    }
}
