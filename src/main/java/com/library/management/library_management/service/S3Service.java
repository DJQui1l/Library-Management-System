package com.library.management.library_management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Service(
        @Value("${aws.s3.region}") String region,
        @Value("${aws.s3.bucket}") String bucketName) {

        this.bucketName = bucketName;


        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {

        String key = "books/" + file.getOriginalFilename();

        // Create a request to upload the file to the folder "books"
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(
                        file.getInputStream(),
                        file.getSize()
                )
        );

        return key;
    }


}
