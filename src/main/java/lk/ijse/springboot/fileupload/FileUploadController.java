package lk.ijse.springboot.fileupload;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file-upload")
public class FileUploadController {
    @GetMapping
    public String test(){
        return "Hello";
    }

    @PostMapping("/upload")
    public void uploadFile(){
        System.out.println("upload invoked");
    }

}
