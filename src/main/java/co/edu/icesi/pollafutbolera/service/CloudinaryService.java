package co.edu.icesi.pollafutbolera.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    public String uploadImage(MultipartFile file) throws IOException;

    public Boolean deleteImage(String publicId) throws IOException;

    public String extractPublicId(String imageUrl);
}
