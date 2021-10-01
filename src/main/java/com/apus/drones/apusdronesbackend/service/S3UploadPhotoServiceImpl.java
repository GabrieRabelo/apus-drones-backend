package com.apus.drones.apusdronesbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.apus.drones.apusdronesbackend.service.dto.UploadPhotoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Slf4j
public class S3UploadPhotoServiceImpl implements S3UploadPhotoService {
    private final AmazonS3 amazonS3;

    public S3UploadPhotoServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public void upload(UploadPhotoDTO request) {
        byte[] fileContent = Base64Utils.decodeFromString(request.getBase64());

        InputStream fileStream = new ByteArrayInputStream(fileContent);

        //nao ta retornando a url...
        //https://www.baeldung.com/aws-s3-java
        //https://www.section.io/engineering-education/spring-boot-amazon-s3/
        PutObjectResult response = amazonS3.putObject("apusdrone", request.getFileName(), fileStream, null);

        String s = ";";
    }
}
