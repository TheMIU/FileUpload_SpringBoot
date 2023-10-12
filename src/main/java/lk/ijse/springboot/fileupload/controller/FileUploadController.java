package lk.ijse.springboot.fileupload.controller;

import lk.ijse.springboot.fileupload.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/file-upload")
public class FileUploadController {
    @Autowired
    private FileUploadService service;

    @PostMapping("/upload")
    public void uploadFile(@RequestParam MultipartFile file) throws IOException {
        System.out.println("upload invoked");
        service.uploadFile(file);
    }

}
