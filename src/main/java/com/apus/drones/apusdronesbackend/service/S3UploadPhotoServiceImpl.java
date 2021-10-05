//package com.apus.drones.apusdronesbackend.service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectResult;
//import com.apus.drones.apusdronesbackend.service.dto.UploadPhotoDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Base64Utils;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//
//@Service
//@Slf4j
//public class S3UploadPhotoServiceImpl implements S3UploadPhotoService {
//    private final AmazonS3 amazonS3;
//
//    public S3UploadPhotoServiceImpl(AmazonS3 amazonS3) {
//        this.amazonS3 = amazonS3;
//    }
//
//    @Override
//    public void upload(UploadPhotoDTO request) {
//        String[] nameSplit = request.getFileName().split("\\.");
//        String extension = "png";
//
//        if (nameSplit.length > 1) {
//            extension = nameSplit[nameSplit.length - 1];
//        }
//
//        byte[] fileContent = Base64Utils.decodeFromString(request.getBase64());
//        InputStream fileStream = new ByteArrayInputStream(fileContent);
//
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(fileContent.length);
//        metadata.setContentType("image/" + extension);
//
//        PutObjectResult response = amazonS3.putObject("apusdrone", request.getFileName(), fileStream, metadata);
//
//        String url = amazonS3.getUrl("apusdrone", request.getFileName()).toString();
//    }
//}
