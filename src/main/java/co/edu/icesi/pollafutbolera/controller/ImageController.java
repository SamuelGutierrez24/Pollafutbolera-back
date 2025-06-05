package co.edu.icesi.pollafutbolera.controller;

import co.edu.icesi.pollafutbolera.api.ImageAPI;
import co.edu.icesi.pollafutbolera.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController implements ImageAPI {

    @Autowired
    public CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadImage(file);
            return ResponseEntity.ok().body(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al subir imagen: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteImage(@RequestParam("publicId") String publicId) {
        try {
            boolean result = cloudinaryService.deleteImage(publicId);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @Override
    public ResponseEntity<String> extractPublicId(@RequestParam("url") String url) {
        try {
            String publicId = cloudinaryService.extractPublicId(url);
            return ResponseEntity.ok().body(publicId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al extraer el public_id: " + e.getMessage());
        }
    }
}
