package lk.ijse.springboot.fileupload.controller;

import lk.ijse.springboot.fileupload.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
@RequestMapping("/file-upload")
public class FileUploadController {
    @Autowired
    private FileUploadService service;

    @Value("${file.upload.path}") // Get the file upload path from properties/configuration
    private String fileUploadPath;

  /*  @PostMapping("/upload")
    public void uploadFile(@RequestParam MultipartFile file) throws IOException {
        System.out.println("upload invoked");
        service.uploadFile(file);
    }*/

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        try {
            String originalFilename = file.getOriginalFilename();
            String filePath = fileUploadPath + File.separator + originalFilename;
            System.out.println(filePath);
            service.uploadFile(file); // Save the file with the specified path
            return ResponseEntity.ok(filePath); // Return the file path in the response
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/upload/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        System.out.println("invoked get");
        // Load the image file and return it as a response
        Path imagePath = Paths.get(fileUploadPath, filename);
        try {
            if (Files.exists(imagePath) && Files.isReadable(imagePath)) {
                Resource resource = new UrlResource(imagePath.toUri());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(imagePath))
                        .body(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

}
