package lk.ijse.springboot.fileupload.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
     void uploadFile(MultipartFile file) throws IOException;
}
