package lk.ijse.springboot.fileupload;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class FileUploadController {
    @GetMapping
    public String test(){
        return "Hello";
    }
}
