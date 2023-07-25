package niit.com.vn.springboot08.controller;

import niit.com.vn.springboot08.entity.Image;
import niit.com.vn.springboot08.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Calendar;
import java.util.List;

@Controller("uploadFileController")
public class UploadFileController {
    @Value("${UPLOAD_DIR}")
    public String uploadDir;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/upload-file")
    public String uploadFile() {
        return "upload-file";
    }

    @PostMapping("/upload-file-handler")
    public String uploadFileHandler(@RequestParam("img") MultipartFile file) {
        try {
            //get current month
            //get current year
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);//lấy về năm
            int month = calendar.get(Calendar.MONTH) + 1;//lấy về tháng
            String newDir = month + "_" + year; //7_2023
            //check xem thư mục mới này đã có trong đường dẫn upload chưa
            File folder = new File(uploadDir + newDir);
            if (!folder.exists() || folder.isFile()) {
                //tạo thư mục mới
                folder.mkdir();
            }
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            FileOutputStream fileOutputStream = new FileOutputStream(uploadDir + newDir + File.separator + fileName);
            fileOutputStream.write(file.getBytes());

            // Save the image information to the database
            Image image = new Image();
            image.setFileName(fileName);
            image.setImageData(file.getBytes());
            imageRepository.save(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Image> images = imageRepository.findAll(); // Fetch all images from the database
        return "upload-file";
    }

    // Rest of the controller code...

    @GetMapping("/display-image/{id}")
    public ResponseEntity<byte[]> displayImage(@PathVariable Long id) {
        // Fetch the image data from the database
        Image image = imageRepository.findById(id).orElse(null);
        if (image != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getImageData());
        }
        return ResponseEntity.notFound().build();
    }

}
