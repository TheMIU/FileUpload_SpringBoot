package lk.ijse.springboot.fileupload.controller;

import lk.ijse.springboot.fileupload.ImageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/file-upload")
public class FileUploadController {
    private final String fileUploadPath = "F:\\\\GitHub MY\\\\FileUpload_SpringBoot\\\\uploads\\\\";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        try {
            String originalFilename = file.getOriginalFilename();
            String filePath = fileUploadPath + originalFilename;
            System.out.println(filePath);
            file.transferTo(new File(fileUploadPath + file.getOriginalFilename()));// Save the file with the specified path
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


    @GetMapping("/list-images")
    public ResponseEntity<List<ImageInfo>> listImages() {
        System.out.println("invoked");

        List<ImageInfo> images = new ArrayList<>();

        try {
            // List all files in the uploads directory
            Files.list(Paths.get(fileUploadPath))
                    .filter(Files::isRegularFile) // Filter out directories
                    .forEach(file -> {
                        String filePath = file.toAbsolutePath().toString();
                        System.out.println(filePath);
                        ImageInfo image = new ImageInfo(file.getFileName().toString(), filePath);
                        images.add(image);
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(images);
    }

}
