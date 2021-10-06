package com.apus.drones.apusdronesbackend.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.apus.drones.apusdronesbackend.service.dto.FileDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Configuration
public class AmazonS3Config {
    private final String bucketName;

    private final AmazonS3 amazonS3;

    public AmazonS3Config(@Value("${aws.s3.bucketName}") String bucketName, @Value("${aws.keys.accessKey}") String accessKey, @Value("${aws.keys.secretKey}") String secretKey) {
        this.bucketName = bucketName;

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        this.amazonS3 =
                AmazonS3ClientBuilder
                        .standard()
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .withRegion(Regions.US_EAST_2)
                        .build();
    }

    public String upload(FileDTO request) {
        String[] nameSplit = request.getFileName().split("\\.");
        String extension = "png";

        if (nameSplit.length > 1) {
            extension = nameSplit[nameSplit.length - 1];
        }

        byte[] fileContent = Base64Utils.decodeFromString(request.getBase64());
        InputStream fileStream = new ByteArrayInputStream(fileContent);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileContent.length);
        metadata.setContentType("image/" + extension);

        PutObjectResult response = amazonS3.putObject(bucketName, request.getFileName(), fileStream, metadata);

        return amazonS3.getUrl(bucketName, request.getFileName()).toString();
    }
}
