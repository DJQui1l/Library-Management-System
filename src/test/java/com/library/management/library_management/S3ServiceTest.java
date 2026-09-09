//package com.library.management.library_management;
//
//import com.library.management.library_management.service.S3Service;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//
//
//@SpringBootTest
//public class S3ServiceTest {
//
//
//    @Autowired
//    private S3Service s3Service;
//
//    @Test
//    void uploadFileTest() throws Exception {
//        String filePath = "C:\\Users\\icebr\\Documents\\Code\\java_programming\\library_management\\book_files\\book_1_test_file.txt";
//        byte[] content = Files.readAllBytes(Paths.get(filePath));
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "book_1_test_file.txt",
//                "text/plain",
//                content
//        );
//
//        String key = s3Service.uploadFile(file);
//
//        System.out.println("S3 Key: " + key);
//    }
//
//}
