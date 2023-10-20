package lk.ijse.springboot.fileupload.controller;

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
    private String uploadsFolderPath;

    public FileUploadController() {
        checkUploadFolderCreated();
    }

    // save image to
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        checkUploadFolderCreated();

        try {
            // file is saving using the original file name, in specific location
            String filePath = uploadsFolderPath + file.getOriginalFilename();
            System.out.println("Saved: " + filePath);

            // Save the file with the specified path
            file.transferTo(new File(filePath));

            // Return the file path in the response
            return ResponseEntity.ok(filePath);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }


    // return image names
    @GetMapping("/list-images")
    public ResponseEntity<List<String>> listImages() {
        // create imageNames list
        List<String> imageNames = new ArrayList<>();

        try {
            // List all files in the uploads directory
            Files.list(Paths.get(uploadsFolderPath))
                    .filter(Files::isRegularFile) // Filter out directories
                    .forEach(file -> {
                        // add each file names to list
                        imageNames.add(file.getFileName().toString());
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(imageNames);
    }


    // return the resource, using file name
    @GetMapping("{filename:.+}") // capture filenames with extensions
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        System.out.println(filename);

        // Load the image file and return it as a response
        Path imagePath = Paths.get(uploadsFolderPath, filename);
        try {
            if (Files.exists(imagePath) && Files.isReadable(imagePath)) {
                Resource resource = new UrlResource(imagePath.toUri());
                System.out.println("resource: " + resource.toString());

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(imagePath))
                        .body(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    //////////////////////////////////////
    public void checkUploadFolderCreated() {
        // Get the absolute path to the project's base directory
        String basePath = new File("").getAbsolutePath();

        // Define the path for the "uploads" folder by appending "uploads" to the base path
        uploadsFolderPath = basePath + File.separator + "uploads" + File.separator;
        System.out.println("uploadsFolderPath : " + uploadsFolderPath);

        // Create a File object to represent the 'uploads' folder based on the specified path
        File uploadsFolder = new File(uploadsFolderPath);

        // Check if the 'uploads' folder exists
        if (!uploadsFolder.exists()) {
            // if not create one
            boolean folderCreated = uploadsFolder.mkdirs();

            // Check if the folder was successfully created
            if (folderCreated) {
                System.out.println("The 'uploads' folder was successfully created.");
            } else {
                System.out.println("Failed to create the 'uploads' folder.");
            }
        } else {
            System.out.println("The 'uploads' folder already exists.");
        }
    }
}


