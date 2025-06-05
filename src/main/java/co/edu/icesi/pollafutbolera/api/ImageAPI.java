package co.edu.icesi.pollafutbolera.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/images")
public interface ImageAPI {

    @PostMapping("/upload")
    ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file);

    @DeleteMapping("/delete")
    ResponseEntity<Boolean> deleteImage(@RequestParam("publicId") String publicId);

    @GetMapping("/extractPublicId")
    ResponseEntity<String> extractPublicId(@RequestParam("url") String url);

}
