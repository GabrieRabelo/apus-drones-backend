package com.apus.drones.apusdronesbackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.apus.drones.apusdronesbackend.service.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Service
public class ImageUploadService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public ImageUploadService(AmazonS3 amazonS3, @Value("${aws.s3.bucketName}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public String upload(FileDTO request) throws SizeLimitExceededException {

        String[] nameSplit = request.getFileName().split("\\.");
        String extension = "png";
        String filename = nameSplit[1] != null ? request.getFileName() : nameSplit[0] + extension;

        if (nameSplit.length > 1) {
            extension = nameSplit[nameSplit.length - 1];
        }

        byte[] fileContent = Base64Utils.decodeFromString(request.getBase64());

        double fileSizeMB = fileContent.length / 1048576D;

        if (fileSizeMB >= 1) {
            throw new SizeLimitExceededException("Image size limit exceeded", fileContent.length, 1048576);
        }

        InputStream fileStream = new ByteArrayInputStream(fileContent);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileContent.length);
        metadata.setContentType("image/" + extension);

        amazonS3.putObject(bucketName, request.getFileName(), fileStream, metadata);

        log.info("Image \"{}\" with size: \"{}MB\" has sent to aws s3 bucket: \"{}\"", filename, fileSizeMB, bucketName);

        return amazonS3.getUrl(bucketName, request.getFileName()).toString();
    }
}
