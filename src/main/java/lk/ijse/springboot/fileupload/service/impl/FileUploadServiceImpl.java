package lk.ijse.springboot.fileupload.service.impl;

import lk.ijse.springboot.fileupload.service.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public void uploadFile(MultipartFile file) throws IOException {
        file.transferTo(new File("F:\\GitHub MY\\FileUpload_SpringBoot\\uploads\\" + file.getOriginalFilename()));
    }
}
